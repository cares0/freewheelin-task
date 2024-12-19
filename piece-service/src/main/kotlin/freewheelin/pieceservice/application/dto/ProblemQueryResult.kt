package freewheelin.pieceservice.application.dto

import freewheelin.common.annotation.DefaultConstructor
import freewheelin.pieceservice.domain.ProblemType

@DefaultConstructor
data class ProblemQueryResult(
    val id: Long,
    val answer: String,
    val unitCode: String,
    val level: Int,
    val problemType: ProblemType,
)