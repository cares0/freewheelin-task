package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.GradePieceForStudentCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult

interface GradePieceForStudentUseCase {

    fun grade(pieceId: Long, command: GradePieceForStudentCommand): List<PieceProblemIdAndResult>

}