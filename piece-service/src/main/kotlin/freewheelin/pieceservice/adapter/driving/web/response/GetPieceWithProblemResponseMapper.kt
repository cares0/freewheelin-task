package freewheelin.pieceservice.adapter.driving.web.response

import freewheelin.common.mapper.Mapper
import freewheelin.pieceservice.application.dto.PieceWithProblemQueryResult
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class GetPieceWithProblemResponseMapper : Mapper<PieceWithProblemQueryResult, GetPieceWithProblemResponse> {
    override val sourceType: KClass<PieceWithProblemQueryResult> = PieceWithProblemQueryResult::class
    override val targetType: KClass<GetPieceWithProblemResponse> = GetPieceWithProblemResponse::class

    override fun map(source: PieceWithProblemQueryResult): GetPieceWithProblemResponse {
        return GetPieceWithProblemResponse(
            pieceId = source.id,
            pieceName = source.name,
            problemCount = source.totalProblemCount,
            pieceProblems = source.pieceProblems.map { pieceProblem ->
                GetPieceWithProblemResponse.PieceProblemResponse(
                    pieceProblemId = pieceProblem.id,
                    problemId = pieceProblem.problemId,
                    number = pieceProblem.number,
                    level = pieceProblem.level,
                    type = pieceProblem.type,
                    contents = pieceProblem.contents,
                )
            },
        )
    }
}