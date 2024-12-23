package freewheelin.pieceservice.adapter.driving.web.response

data class AnalyzePieceResponse(
    val pieceId: Long,
    val pieceName: String,
    val publishedStudentIds: Set<String>,
    val studentStats: List<StudentStatResponse>,
    val problemStats: List<ProblemStatResponse>,
) {
    data class StudentStatResponse(
        val studentId: String,
        val solvedPercentage: Int,
    )

    data class ProblemStatResponse(
        val problemId: Long,
        val number: Int,
        val solvedStudentPercentage: Int,
    )
}