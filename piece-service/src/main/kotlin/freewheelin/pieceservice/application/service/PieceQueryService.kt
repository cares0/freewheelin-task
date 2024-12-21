package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.PieceWithProblemQueryResult
import freewheelin.pieceservice.application.port.inbound.GetPieceWithProblemUseCase
import freewheelin.pieceservice.application.port.outbound.PieceQueryPort
import org.springframework.stereotype.Service

@Service
class PieceQueryService(
    private val pieceQueryPort: PieceQueryPort,
) : GetPieceWithProblemUseCase {

    override fun queryWithProblemById(pieceId: Long): PieceWithProblemQueryResult {
        return pieceQueryPort.queryWithProblemByPieceId(pieceId)
    }

}