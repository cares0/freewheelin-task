package freewheelin.pieceservice.adapter.driving.web.response

import freewheelin.common.mapper.Mapper
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class GradePieceResponseMapper : Mapper<PieceProblemIdAndResult, GradePieceResponse> {
    override val sourceType: KClass<PieceProblemIdAndResult> = PieceProblemIdAndResult::class
    override val targetType: KClass<GradePieceResponse> = GradePieceResponse::class

    override fun map(source: PieceProblemIdAndResult): GradePieceResponse {
        return GradePieceResponse(
            pieceProblemId = source.pieceProblemId,
            isSolved = source.isSolved,
        )
    }
}