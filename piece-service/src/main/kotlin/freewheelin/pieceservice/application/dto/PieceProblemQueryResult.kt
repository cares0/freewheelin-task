package freewheelin.pieceservice.application.dto

import freewheelin.common.annotation.DefaultConstructor
import freewheelin.pieceservice.domain.ProblemType

@DefaultConstructor
data class PieceProblemQueryResult(
    val id: Long,
    val problemId: Long,
    val number: Int,
    val unitCode: String,
    val level: Int,
    val type: ProblemType,
    val contents: String,
    val answer: String,
)