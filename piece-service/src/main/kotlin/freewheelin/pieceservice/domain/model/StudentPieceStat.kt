package freewheelin.pieceservice.domain.model

import java.util.UUID

data class StudentPieceStat(
    val studentPieceId: Long,
    val studentId: UUID,
) {

    val pieceProblemIdToResultMap: MutableMap<Long, GradeResult> = mutableMapOf()
    var solvedProblemRate: Double = 0.0

    fun applyResult(pieceProblemId: Long, gradeResult: GradeResult) {
        pieceProblemIdToResultMap[pieceProblemId] = gradeResult
    }

    fun refreshSolvedProblemRate(totalProblemCount: Int) {
        val solvedProblemCount = pieceProblemIdToResultMap
            .filter { it.value == GradeResult.SOLVED }
            .count()

        this.solvedProblemRate = solvedProblemCount / totalProblemCount.toDouble()
    }

}