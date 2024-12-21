package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.ProblemQueryCondition
import freewheelin.pieceservice.application.dto.ProblemQueryResult
import freewheelin.pieceservice.application.port.inbound.GetProblemByConditionUseCase
import freewheelin.pieceservice.application.port.outbound.ProblemConditionQueryPort
import freewheelin.pieceservice.application.port.outbound.ProblemGroupQueryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Service
@Transactional(readOnly = true)
class ProblemQueryService(
    private val problemGroupQueryPort: ProblemGroupQueryPort,
    private val problemConditionQueryPort: ProblemConditionQueryPort,
) : GetProblemByConditionUseCase {

    override fun queryByCondition(
        condition: ProblemQueryCondition,
    ): List<ProblemQueryResult> {
        // 카테고리 별 가용 가능한 문제 수를 구합니다.
        val categoryToAvailableCountMap = getAvailableProblemCountPerCategory(condition)
        
        // 총 조회해야 할 문제 수를 초기화 합니다.
        val totalProblemCountToQuery = initTotalProblemCountToQuery(categoryToAvailableCountMap, condition.totalCount)
        
        // 카테고리 별로 문제를 할당합니다.
        val categoryToAllocatedCountMap = allocatePerCategory(
            mainCategory = condition.levelCategory,
            totalProblemCountToQuery = totalProblemCountToQuery,
            categoryToAvailableCountMap = categoryToAvailableCountMap
        )

        // 할당된 정보를 바탕으로 문제를 쿼리합니다.
        return queryByConditionWithAllocation(categoryToAllocatedCountMap, condition)
            .sortedWith(compareBy(
                { it.unitCode },
                { it.problemType },
                { it.level }
            ))
    }

    private fun getAvailableProblemCountPerCategory(condition: ProblemQueryCondition): Map<ProblemLevelCategory, Int> {
        // 레벨 별 가용 가능한 문제 수를 구합니다.
        val levelToAvailableCountMap = problemGroupQueryPort.queryAvailableProblemCountGroupedByLevel(
            unitCodes = condition.unitCodeList,
            problemType = condition.type,
        )

        // 레벨로 그룹지어진 Map을, 카테고리로 그룹을 바꾸어 카테고리 별로 가용 가능한 문제 수를 구합니다.
        return levelToAvailableCountMap.entries
            .groupBy { (level, _) -> ProblemLevelCategory.fromLevel(level) }
            .mapValues { (_, entries) -> entries.sumOf { it.value } }
    }
    
    private fun initTotalProblemCountToQuery(
        categoryToAvailableCountMap: Map<ProblemLevelCategory, Int>,
        requestedTotalCount: Int,
    ): Int {
        // 총 가용 가능한 문제수를 구합니다.
        val totalAvailableCount = categoryToAvailableCountMap.values.sum()

        // 총 가용 가능한 문제 수와, 요청된 총 문제 수 중 작은 값을 남은 문제 수로 초기화 합니다.
        return min(requestedTotalCount, totalAvailableCount)
    }

    private fun allocatePerCategory(
        mainCategory: ProblemLevelCategory,
        totalProblemCountToQuery: Int,
        categoryToAvailableCountMap: Map<ProblemLevelCategory, Int>
    ): MutableMap<ProblemLevelCategory, Int> {
        // 남은 문제수를 초기화 합니다.
        var remainingCount = totalProblemCountToQuery

        // 카테고리 별로 할당된 문제 수를 담는 Map을 초기화 합니다.
        val categoryToAllocatedCountMap = mutableMapOf<ProblemLevelCategory, Int>()
        // 카테고리 별로 가용 가능한 남은 문제수를 저장하는 Map을 초기화 합니다.
        val categoryToRemainingAvailableCountMap = categoryToAvailableCountMap.toMutableMap()

        // 비율이 높은 순으로 카테고리를 정렬하여, 우선 순위를 구합니다.
        val categoryPriority = getCategoryPriorityFromRatio(mainCategory.getRatioMap())

        // 남은 문제 수가 없어질 때 까지(문제가 모두 할당 될 때 까지) 반복합니다.
        while (remainingCount > 0) {
            // 우선 할당해야 할 카테고리 순서대로 반복합니다.
            categoryPriority.forEach { category ->
                // 카테고리 별 가용 가능한 문제 수를 구합니다.
                val availableCount = categoryToRemainingAvailableCountMap[category] ?: 0

                // 만약 가용 가능한 문제 수가 없거나, 남아있는 문제 수가 없다면 계산 로직을 타지 않습니다.
                if (availableCount <= 0 || remainingCount <= 0) return@forEach

                // 남아있는 문제 수와, 카테고리에 할당 된 비율을 통해 할당 가능한 수를 구합니다.
                val allocableCount = getAllocableCount(
                    remainingCount = remainingCount,
                    ratio = mainCategory.getRatioMap()[category]!!,
                )

                // 가용 가능한 수와, 할당 가능한 수 중 최소 값을 할당할 수로 계산합니다.
                val allocatedCount = min(availableCount, allocableCount)

                // 카테고리 별 할당할 문제 수에 할당할 수를 추가합니다.
                categoryToAllocatedCountMap.merge(category, allocatedCount) { old, new -> old + new }

                // 남은 문제 수에 할당한 수를 차감합니다.
                remainingCount -= allocatedCount

                // 가용 가능한 문제 수에서 할당한 문제 수를 차감합니다.
                categoryToRemainingAvailableCountMap[category] = availableCount - allocatedCount
            }
        }
        return categoryToAllocatedCountMap
    }


    private fun getCategoryPriorityFromRatio(
        ratioMap: Map<ProblemLevelCategory, Double>
    ): List<ProblemLevelCategory> {
        return ratioMap.entries
            .sortedByDescending { (_, ratio) -> ratio }
            .map { (category, _) -> category }
    }

    private fun getAllocableCount(
        remainingCount: Int,
        ratio: Double,
    ): Int {
        // 남아있는 문제 수와 비율에 비례한 할당 가능한 문제 수를 구합니다. 만약 1보다 작은 경우, 1개로 계산합니다.
        return max(1, floor(remainingCount * ratio).toInt())
    }

    private fun queryByConditionWithAllocation(
        categoryToAllocatedCountMap: MutableMap<ProblemLevelCategory, Int>,
        condition: ProblemQueryCondition
    ) = categoryToAllocatedCountMap.flatMap { (category, allocatedCount) ->
        problemConditionQueryPort.queryByUnitCodesAndTypeAndRangeWithLimit(
            unitCodes = condition.unitCodeList,
            problemType = condition.type,
            levelRange = category.levelRange,
            limit = allocatedCount
        )
    }

}