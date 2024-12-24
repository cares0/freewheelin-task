package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.GradePieceBatchCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult

interface GradePieceUseCase {

    fun gradeBatch(pieceId: Long, command: GradePieceBatchCommand): List<PieceProblemIdAndResult>

}