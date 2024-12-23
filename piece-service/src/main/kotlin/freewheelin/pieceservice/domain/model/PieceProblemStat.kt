package freewheelin.pieceservice.domain.model

data class PieceProblemStat(
    val pieceProblemId: Long,
    val problemId: Long,
    val number: Int,
) {

    val pieceStudentIdToGradeResultMap: MutableMap<String, GradeResult> = mutableMapOf()

    var solvedStudentRate: Double = 0.0

    fun applyResult(studentId: String, gradeResult: GradeResult) {
        pieceStudentIdToGradeResultMap[studentId] = gradeResult
    }

    fun refreshSolvedStudentRate(totalStudentCount: Int) {
        val solvedStudentCount = pieceStudentIdToGradeResultMap
            .filter { it.value == GradeResult.SOLVED }
            .count()
        this.solvedStudentRate = solvedStudentCount / totalStudentCount.toDouble()
    }

}