package freewheelin.pieceservice.application.dto

import freewheelin.common.annotation.DefaultConstructor

@DefaultConstructor
data class PieceWithProblemQueryResult(
    val id: Long,
    val name: String,
    val totalProblemCount: Int,
    val problems: List<PieceProblemQueryResult>
)