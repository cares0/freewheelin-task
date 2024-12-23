package freewheelin.pieceservice.application.dto

import java.util.UUID

data class GradePieceForStudentCommand(
    val studentId: UUID,
    val problemIdAndAnswers: List<PieceProblemIdAndAnswer>
)