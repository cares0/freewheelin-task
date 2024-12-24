package freewheelin.pieceservice.domain.model

class PieceStat(

    val piece: Piece,

    var studentStats: MutableList<StudentPieceStat> = mutableListOf(),

    var problemStats: MutableList<PieceProblemStat> = mutableListOf(),

) {

    val id: Long = piece.id

    fun applyGradeResult(
        gradedStudentPiece: StudentPiece,
        gradeResultPerPieceProblem: Map<PieceProblem, GradeResult>,
    ) {
        val targetStudentStat = resolveOrCreateStudentStat(gradedStudentPiece)

        // ProblemStat 매핑 생성
        val problemStatMap = problemStats.associateBy { it.pieceProblemId }

        // 각 문제에 대해 결과 반영
        gradeResultPerPieceProblem.forEach { (pieceProblem, gradeResult) ->
            // 학생 통계에 결과 반영
            targetStudentStat.applyResult(pieceProblem.id, gradeResult)

            // 문제 통계들에 결과 적용
            val targetProblemStat = resolveFromMapOrCreateProblemStat(problemStatMap, pieceProblem)
            targetProblemStat.applyResult(
                studentId = gradedStudentPiece.studentId.toString(),
                gradeResult = gradeResult
            )

            // 문제 통계 갱신
            targetProblemStat.refreshSolvedStudentRate(piece.totalStudentCount)
        }

        // 학생 통계 갱신
        targetStudentStat.refreshSolvedProblemRate(piece.totalProblemCount)
    }

    private fun resolveOrCreateStudentStat(gradedStudentPiece: StudentPiece): StudentPieceStat {
        return studentStats.firstOrNull { it.studentPieceId == gradedStudentPiece.id }
            ?: createAndAddStudentStat(gradedStudentPiece)
    }

    private fun createAndAddStudentStat(gradedStudentPiece: StudentPiece): StudentPieceStat {
        return StudentPieceStat(
            studentPieceId = gradedStudentPiece.id,
            studentId = gradedStudentPiece.studentId
        ).also { studentStats.add(it) }
    }

    private fun resolveFromMapOrCreateProblemStat(
        problemStatMap: Map<Long, PieceProblemStat>,
        pieceProblem: PieceProblem
    ): PieceProblemStat {
        return problemStatMap[pieceProblem.id] ?: createAndAddProblemStat(pieceProblem)
    }

    private fun createAndAddProblemStat(pieceProblem: PieceProblem): PieceProblemStat {
        return PieceProblemStat(
            pieceProblemId = pieceProblem.id,
            problemId = pieceProblem.problem.id,
            number = pieceProblem.number
        ).also { problemStats.add(it) }
    }

    fun initStudentStats(studentPieces: List<StudentPiece>) = studentPieces.forEach(::createAndAddStudentStat)

    companion object {

        fun of(piece: Piece): PieceStat {
            return PieceStat(
                piece = piece,
                problemStats = piece.pieceProblems.map { pieceProblem ->
                    PieceProblemStat(
                        pieceProblemId = pieceProblem.id,
                        problemId = pieceProblem.problem.id,
                        number = pieceProblem.number,
                    )
                }.toMutableList()
            )
        }

    }

}