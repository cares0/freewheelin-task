package freewheelin.pieceservice.application.dto

import freewheelin.pieceservice.domain.model.GradeResult

data class PieceProblemIdAndResult(
    val pieceProblemId: Long,
    val result: GradeResult,
)