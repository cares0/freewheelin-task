package freewheelin.pieceservice.adapter.driving.web.response

data class GetProblemByConditionResponse(
    val problemList: List<ProblemResponse>
) {

    data class ProblemResponse(
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
}