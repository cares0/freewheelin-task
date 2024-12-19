package freewheelin.pieceservice.adapter.driving.web.request

data class GetProblemByConditionRequest(
    val totalCount: Int,
    val unitCodeList: List<String>,
    val level: Level,
    val problemType: ProblemType,
) {
    enum class ProblemType {
        ALL,
        SUBJECTIVE,
        SELECTION,
    }

    enum class Level {
        LOW,
        MIDDLE,
        HIGH,
    }
}