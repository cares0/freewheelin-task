package freewheelin.pieceservice.adapter.driving.web.response

data class GetProblemByConditionResponse(
    val id: Long,
    val answer: String,
    val unitCode: String,
    val level: Int,
    val problemType: ProblemType,
) {

    enum class ProblemType {
        SELECTION,
        SUBJECTIVE,
    }
}