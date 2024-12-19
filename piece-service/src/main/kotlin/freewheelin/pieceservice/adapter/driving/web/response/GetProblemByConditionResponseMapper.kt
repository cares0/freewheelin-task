package freewheelin.pieceservice.adapter.driving.web.response

import freewheelin.common.mapper.Mapper
import freewheelin.common.supports.toOtherEnumOrNull
import freewheelin.pieceservice.adapter.driving.web.request.GetProblemByConditionRequest
import freewheelin.pieceservice.application.dto.ProblemQueryResult
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class GetProblemByConditionResponseMapper : Mapper<ProblemQueryResult, GetProblemByConditionResponse> {
    override val sourceType: KClass<ProblemQueryResult> = ProblemQueryResult::class
    override val targetType: KClass<GetProblemByConditionResponse> = GetProblemByConditionResponse::class

    override fun map(source: ProblemQueryResult): GetProblemByConditionResponse {
        return GetProblemByConditionResponse(
            id = source.id,
            answer = source.answer,
            unitCode = source.unitCode,
            level = source.level,
            problemType = source.problemType
                .toOtherEnumOrNull<GetProblemByConditionResponse.ProblemType>()!!,
        )
    }
}