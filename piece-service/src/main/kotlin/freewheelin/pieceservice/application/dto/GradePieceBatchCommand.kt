package freewheelin.pieceservice.application.dto

import java.util.UUID

data class GradePieceBatchCommand(
    val studentId: UUID,
    val problemIdAndAnswers: List<PieceProblemIdAndAnswer>
)