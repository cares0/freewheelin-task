package freewheelin.pieceservice.application.dto

data class AnalyzePieceResult(
    val pieceId: Long,
    val pieceName: String,
    val studentResults: List<StudentAnalyzeResult>,
    val problemResults: List<ProblemAnalyzeResult>,
) {

    data class StudentAnalyzeResult(
        val studentId: String,
        val solvedRate: Double,
    )

    data class ProblemAnalyzeResult(
        val problemId: Long,
        val number: Int,
        val solvedRate: Double,
    )

}