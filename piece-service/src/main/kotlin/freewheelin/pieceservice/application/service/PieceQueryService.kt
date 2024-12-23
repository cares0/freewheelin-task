package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.PieceWithProblemQueryResult
import freewheelin.pieceservice.application.port.inbound.AnalyzePieceUseCase
import freewheelin.pieceservice.application.port.inbound.GetPieceWithProblemUseCase
import freewheelin.pieceservice.application.port.outbound.PieceLoadPort
import freewheelin.pieceservice.application.port.outbound.PieceQueryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PieceQueryService(
    private val pieceQueryPort: PieceQueryPort,
) : GetPieceWithProblemUseCase {

    override fun queryWithProblemById(pieceId: Long): PieceWithProblemQueryResult {
        return pieceQueryPort.queryWithProblemByPieceId(pieceId)
    }

}