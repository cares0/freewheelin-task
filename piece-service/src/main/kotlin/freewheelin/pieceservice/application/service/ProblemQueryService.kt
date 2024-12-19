package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.domain.ProblemLevelCategory
import freewheelin.pieceservice.application.dto.ProblemQueryCondition
import freewheelin.pieceservice.application.dto.ProblemQueryResult
import freewheelin.pieceservice.application.port.inbound.GetProblemByConditionUseCase
import freewheelin.pieceservice.application.port.outbound.ProblemQueryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

@Service
@Transactional(readOnly = true)
class ProblemQueryService(
    private val problemQueryPort: ProblemQueryPort,
) : GetProblemByConditionUseCase {

    override fun queryByCondition(
        condition: ProblemQueryCondition,
    ): List<ProblemQueryResult> {
        // 레벨의 카테고리 별로 레벨의 범위 별 비율을 구합니다.
        val levelRatioMap = resolveLevelRangeToRatioMap(condition.levelCategory)

        // 비율에 따라 문제를 조회합니다.
        return levelRatioMap.flatMap { (levelRange, ratio) ->
            val list = problemQueryPort.queryByUnitCodesAndTypeAndRangeWithLimit(
                unitCodes = condition.unitCodeList,
                problemType = condition.type,
                levelRange = levelRange,
                limit = ceil(condition.totalCount * ratio).toInt(),
            )
            list
        }.take(condition.totalCount)
    }

    private fun resolveLevelRangeToRatioMap(levelCategory: ProblemLevelCategory) =
        when (levelCategory) {
            ProblemLevelCategory.LOW -> makeRatioMap(
                lowLevelRatio = 0.5,
                middleLevelRatio = 0.3,
                highLevelRatio = 0.2
            )
            ProblemLevelCategory.MIDDLE -> makeRatioMap(
                lowLevelRatio = 0.25,
                middleLevelRatio = 0.5,
                highLevelRatio = 0.25
            )
            ProblemLevelCategory.HIGH -> makeRatioMap(
                lowLevelRatio = 0.2,
                middleLevelRatio = 0.5,
                highLevelRatio = 0.3
            )
        }

    private fun makeRatioMap(
        lowLevelRatio: Double,
        middleLevelRatio: Double,
        highLevelRatio: Double
    ) = mapOf(
        ProblemLevelCategory.LOW.levelRange to lowLevelRatio,
        ProblemLevelCategory.MIDDLE.levelRange to middleLevelRatio,
        ProblemLevelCategory.HIGH.levelRange to highLevelRatio,
    )

}