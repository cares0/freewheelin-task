package freewheelin.pieceservice.adapter.driving.web.request

data class GradePieceRequest(
    val studentId: String,
    val problemIdAndAnswers: List<PieceProblemIdAndAnswer>
) {

    data class PieceProblemIdAndAnswer(
        val pieceProblemId: Long,
        val answer: String,
    )
}