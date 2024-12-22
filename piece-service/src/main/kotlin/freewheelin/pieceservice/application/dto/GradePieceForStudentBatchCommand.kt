package freewheelin.pieceservice.application.dto

import java.util.UUID

data class GradePieceForStudentBatchCommand(
    val studentId: UUID,
    val problemIdAndAnswers: List<PieceProblemIdAndAnswer>
)