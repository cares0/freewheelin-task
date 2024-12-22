package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.GradePieceForStudentBatchCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult

interface GradePieceForStudentUseCase {

    fun gradeBatch(pieceId: Long, command: GradePieceForStudentBatchCommand): List<PieceProblemIdAndResult>

}