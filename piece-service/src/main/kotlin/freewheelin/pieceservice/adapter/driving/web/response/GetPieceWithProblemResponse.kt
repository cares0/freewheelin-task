package freewheelin.pieceservice.adapter.driving.web.response

import freewheelin.pieceservice.domain.model.ProblemType

data class GetPieceWithProblemResponse(
    val pieceId: Long,
    val pieceName: String,
    val problemCount: Int,
    val pieceProblems: List<PieceProblemResponse>
) {
    data class PieceProblemResponse(
        val pieceProblemId: Long,
        val problemId: Long,
        val number: Int,
        val level: Int,
        val contents: String,
        val type: ProblemType,
    )
}