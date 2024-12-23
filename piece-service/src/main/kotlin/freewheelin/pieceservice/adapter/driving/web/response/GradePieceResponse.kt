package freewheelin.pieceservice.adapter.driving.web.response

import freewheelin.pieceservice.domain.model.GradeResult

data class GradePieceResponse(
    val pieceProblemId: Long,
    val result: GradeResult,
)