package freewheelin.pieceservice.adapter.driving.web.request

import freewheelin.common.mapper.Mapper
import freewheelin.common.supports.toOtherEnumOrNull
import freewheelin.pieceservice.application.service.ProblemLevelCategory
import freewheelin.pieceservice.domain.model.ProblemType
import freewheelin.pieceservice.application.dto.ProblemQueryCondition
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class GetProblemByConditionRequestMapper : Mapper<GetProblemByConditionRequest, ProblemQueryCondition> {
    override val sourceType: KClass<GetProblemByConditionRequest> = GetProblemByConditionRequest::class
    override val targetType: KClass<ProblemQueryCondition> = ProblemQueryCondition::class

    override fun map(source: GetProblemByConditionRequest): ProblemQueryCondition {
        return ProblemQueryCondition(
            totalCount = source.totalCount,
            unitCodeList = source.unitCodeList,
            levelCategory = source.level.toOtherEnumOrNull<ProblemLevelCategory>()!!,
            type = source.problemType.toOtherEnumOrNull<ProblemType>()
        )
    }
}