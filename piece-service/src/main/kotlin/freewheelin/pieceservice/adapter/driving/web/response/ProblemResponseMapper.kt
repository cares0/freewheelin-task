package freewheelin.pieceservice.adapter.driving.web.response

import freewheelin.common.mapper.Mapper
import freewheelin.common.supports.toOtherEnumOrNull
import freewheelin.pieceservice.application.dto.ProblemQueryResult
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class ProblemResponseMapper : Mapper<ProblemQueryResult, GetProblemByConditionResponse.ProblemResponse> {
    override val sourceType: KClass<ProblemQueryResult> = ProblemQueryResult::class
    override val targetType: KClass<GetProblemByConditionResponse.ProblemResponse> =
        GetProblemByConditionResponse.ProblemResponse::class

    override fun map(source: ProblemQueryResult): GetProblemByConditionResponse.ProblemResponse {
        return GetProblemByConditionResponse.ProblemResponse(
            id = source.id,
            answer = source.answer,
            unitCode = source.unitCode,
            level = source.level,
            problemType = source.problemType
                .toOtherEnumOrNull<GetProblemByConditionResponse.ProblemResponse.ProblemType>()!!,
        )
    }
}