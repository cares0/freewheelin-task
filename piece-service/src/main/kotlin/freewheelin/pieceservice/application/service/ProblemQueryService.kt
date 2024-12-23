package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.ProblemQueryCondition
import freewheelin.pieceservice.application.dto.ProblemQueryResult
import freewheelin.pieceservice.application.port.inbound.GetProblemByConditionUseCase
import freewheelin.pieceservice.application.port.outbound.ProblemConditionQueryPort
import freewheelin.pieceservice.application.port.outbound.ProblemGroupQueryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

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
        val availableCountPerCategory = calculateAvailableProblemCountPerCategory(condition)
        
        // 총 조회해야 할 문제 수를 초기화 합니다.
        val totalProblemCountToQuery = initTotalProblemCountToQuery(availableCountPerCategory, condition.totalCount)
        
        // 카테고리 별로 문제를 할당합니다.
        val allocatedCountPerCategory = allocateCountPerCategory(
            mainCategory = condition.levelCategory,
            totalProblemCountToQuery = totalProblemCountToQuery,
            availableCountPerCategory = availableCountPerCategory
        )

        // 할당된 정보를 바탕으로 문제를 쿼리합니다.
        return queryProblemsByAllocation(allocatedCountPerCategory, condition)
            .sortedWith(compareBy(
                { it.unitCode },
                { it.problemType },
                { it.level }
            ))
    }

    private fun calculateAvailableProblemCountPerCategory(condition: ProblemQueryCondition): Map<ProblemLevelCategory, Int> {
        // 레벨 별 가용 가능한 문제 수를 구합니다.
        val levelToAvailableCountMap = problemGroupQueryPort.queryAvailableProblemCountGroupedByLevel(
            unitCodes = condition.unitCodeList,
            problemType = condition.type,
        )

        // 카테고리에 해당되는 level을 모두 합산하여 카테고리로 그룹지어진 가용 가능한 문제 수를 구합니다.
        return ProblemLevelCategory.entries.associateWith { category ->
            category.levelRange.sumOf { level -> levelToAvailableCountMap[level] ?: 0 }
        }
    }
    
    private fun initTotalProblemCountToQuery(
        availableCountPerCategory: Map<ProblemLevelCategory, Int>,
        requestedTotalCount: Int,
    ): Int {
        // 총 가용 가능한 문제수를 구합니다.
        val totalAvailableCount = availableCountPerCategory.values.sum()

        // 총 가용 가능한 문제 수와, 요청된 총 문제 수 중 작은 값을 남은 문제 수로 초기화 합니다.
        return min(requestedTotalCount, totalAvailableCount)
    }

    private fun allocateCountPerCategory(
        mainCategory: ProblemLevelCategory,
        totalProblemCountToQuery: Int,
        availableCountPerCategory: Map<ProblemLevelCategory, Int>
    ): MutableMap<ProblemLevelCategory, Int> {
        // 남은 문제수를 초기화 합니다.
        var remainingCount = totalProblemCountToQuery

        // 카테고리 별로 할당된 문제 수를 담는 Map을 초기화 합니다.
        val allocatedCountPerCategory = mutableMapOf<ProblemLevelCategory, Int>()

        // 카테고리 별로 가용 가능한 남은 문제수를 저장하는 Map을 초기화 합니다.
        val remainingAvailableCountPerCategory = availableCountPerCategory.toMutableMap()

        // 카테고리 별 가용 가능한 문제가 없는 경우, 비율을 조정하여 조정된 비율을 담는 Map을 만듭니다.
        val adjustedRatioMap = mainCategory.getRatioMap().toMutableMap()

        // 남은 문제 수가 없어질 때 까지(문제가 모두 할당 될 때 까지) 반복합니다.
        while (remainingCount > 0) {
            // 가용 가능한 문제가 없는 카테고리를 없애고, 나머지 카테고리 안에서 비율을 조정합니다.
            adjustRatiosForUnavailableCategories(adjustedRatioMap, remainingAvailableCountPerCategory)

            // 이번 루프에서 비율에 적용되어야 하는 기준 문제 수를 초기화 합니다.
            val baseProblemCountInLoop = remainingCount

            // 카테고리 별 가용 가능한 문제 수를 구합니다.
            adjustedRatioMap.forEach { (category, ratio) ->
                allocateCountToCategory(
                    category = category,
                    remainingCount = remainingCount,
                    baseCount = baseProblemCountInLoop,
                    ratio = ratio,
                    remainingAvailableCountPerCategory = remainingAvailableCountPerCategory,
                    allocatedCountPerCategory = allocatedCountPerCategory,
                ).also { allocated ->
                    remainingCount -= allocated
                }
            }

        }
        return allocatedCountPerCategory
    }

    private fun allocateCountToCategory(
        category: ProblemLevelCategory,
        remainingCount: Int,
        baseCount: Int,
        ratio: Double,
        remainingAvailableCountPerCategory: MutableMap<ProblemLevelCategory, Int>,
        allocatedCountPerCategory: MutableMap<ProblemLevelCategory, Int>
    ): Int {
        val availableCount = remainingAvailableCountPerCategory[category] ?: 0
        if (availableCount <= 0 || remainingCount <= 0) return 0

        val allocableCount = calculateAllocableCount(baseCount, ratio)
        val allocatedCount = min(remainingCount, min(availableCount, allocableCount))

        allocatedCountPerCategory.merge(category, allocatedCount) { old, new -> old + new }
        remainingAvailableCountPerCategory[category] = availableCount - allocatedCount

        return allocatedCount
    }

    private fun adjustRatiosForUnavailableCategories(
        adjustedRatioMap: MutableMap<ProblemLevelCategory, Double>,
        remainingAvailableCountPerCategory: Map<ProblemLevelCategory, Int>
    ) {
        val unavailableCategories = remainingAvailableCountPerCategory
            .filterValues { it == 0 }
            .keys

        unavailableCategories.forEach(adjustedRatioMap::remove)
        val totalRemainingRatio = adjustedRatioMap.values.sum()

        adjustedRatioMap.replaceAll { _, ratio -> ratio / totalRemainingRatio }
    }

    private fun calculateAllocableCount(
        baseCount: Int,
        ratio: Double,
    ): Int {
        // 비율에 비례한 할당 가능한 문제 수를 구합니다. 만약 1보다 작은 경우, 1개로 계산합니다.
        return max(1, round(baseCount * ratio).toInt())
    }

    private fun queryProblemsByAllocation(
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