package freewheelin.pieceservice.domain.model

class PieceStat(

    val piece: Piece,

    var studentStats: MutableList<StudentPieceStat> = mutableListOf(),

    var problemStats: MutableList<PieceProblemStat> = mutableListOf(),

) {

    val id: Long = piece.id

    fun applyGradeResult(
        gradedStudentPiece: StudentPiece,
        pieceProblemToGradeResultMap: Map<PieceProblem, GradeResult>,
    ) {
        val targetStudentStat = resolveTargetStudentStat(gradedStudentPiece)

        val problemStatMap = problemStats.associateBy { it.pieceProblemId }

        pieceProblemToGradeResultMap.forEach { (pieceProblem, gradeResult) ->
            targetStudentStat.applyResult(pieceProblem.id, gradeResult)

            val targetProblemStat = resolveTargetProblemStatFromMap(problemStatMap, pieceProblem)

            targetProblemStat.applyResult(
                studentId = gradedStudentPiece.studentId.toString(),
                gradeResult = gradeResult
            )

            targetProblemStat.refreshSolvedStudentRate(piece.totalStudentCount)
        }

        targetStudentStat.refreshSolvedProblemRate(piece.totalProblemCount)
    }

    private fun resolveTargetStudentStat(gradedStudentPiece: StudentPiece): StudentPieceStat {
        var studentPieceStat = studentStats.firstOrNull { it.studentPieceId == gradedStudentPiece.id }
        if (studentPieceStat == null) {
            val newStat = StudentPieceStat(
                studentPieceId = gradedStudentPiece.id,
                studentId = gradedStudentPiece.studentId,
            )
            this.studentStats.add(newStat)
            studentPieceStat = newStat
        }
        return studentPieceStat
    }

    private fun resolveTargetProblemStatFromMap(
        problemStatMap: Map<Long, PieceProblemStat>,
        pieceProblem: PieceProblem
    ): PieceProblemStat {
        var pieceProblemStat = problemStatMap[pieceProblem.id]
        if (pieceProblemStat == null) {
            val newStat = PieceProblemStat(
                pieceProblemId = pieceProblem.id,
                problemId = pieceProblem.problem.id,
                number = pieceProblem.number,
            )
            this.problemStats.add(newStat)
            pieceProblemStat = newStat
        }
        return pieceProblemStat
    }

    fun initStudentStats(
        studentPiece: List<StudentPiece>
    ) {
        this.studentStats.addAll(
            studentPiece.map {
                StudentPieceStat(
                    studentPieceId = it.id,
                    studentId = it.studentId,
                )
            }
        )
    }

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