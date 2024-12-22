package freewheelin.pieceservice.domain

import freewheelin.common.supports.EntityBase
import jakarta.persistence.*
import jakarta.persistence.FetchType.*
import jakarta.persistence.GenerationType
import java.util.*

@Entity
class StudentPiece private constructor(

    @Column(columnDefinition = "BINARY(16)")
    val studentId: UUID,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "piece_id")
    val piece: Piece,

) : EntityBase() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_piece_id", insertable = false, updatable = false)
    val id: Long = 0

    private var solvedPieceProblemIdJoined: String? = null
    private var failedPieceProblemIdJoined: String? = null

    val solvedPieceProblemIds: List<Long>
        get() = this.solvedPieceProblemIdJoined?.split(", ")?.map(String::toLong) ?: emptyList()

    val failedPieceProblemIds: List<Long>
        get() = this.failedPieceProblemIdJoined?.split(", ")?.map(String::toLong) ?: emptyList()

    fun grade(
        pieceProblem: PieceProblem,
        submittedAnswer: String,
    ): Boolean {
        validatePieceProblem(pieceProblem)

        return if (pieceProblem.problem.answer == submittedAnswer) {
            gradeAsSolved(pieceProblem)
            true
        } else {
            gradeAsFailed(pieceProblem)
            false
        }
    }

    private fun validatePieceProblem(pieceProblem: PieceProblem) {
        if (pieceProblem.piece.id != this.piece.id) throw CannotGradePieceException()
    }

    private fun gradeAsSolved(
        pieceProblem: PieceProblem,
    ) {
        removeFailedProblem(pieceProblem)
        addSolvedProblem(pieceProblem)
        pieceProblem.addSolvedStudent(this.studentId.toString())
    }

    private fun gradeAsFailed(
        pieceProblem: PieceProblem
    ) {
        removeSolvedProblem(pieceProblem)
        addFailedProblem(pieceProblem)
        pieceProblem.addFailedStudent(this.studentId.toString())
    }

    private fun addSolvedProblem(pieceProblem: PieceProblem) {
        this.solvedPieceProblemIdJoined =
            solvedPieceProblemIds.plus(pieceProblem.id).joinToString(", ")
    }

    private fun addFailedProblem(pieceProblem: PieceProblem) {
        this.failedPieceProblemIdJoined =
            failedPieceProblemIds.plus(pieceProblem.id).joinToString(", ")
    }

    private fun removeSolvedProblem(pieceProblem: PieceProblem) {
        val removedList = solvedPieceProblemIds.filterNot { it == pieceProblem.id }

        if (removedList.isNotEmpty()) {
            this.solvedPieceProblemIdJoined = removedList.joinToString(", ")
        }
    }

    private fun removeFailedProblem(pieceProblem: PieceProblem) {
        val removedList = failedPieceProblemIds.filterNot { it == pieceProblem.id }

        if (removedList.isNotEmpty()) {
            this.failedPieceProblemIdJoined = failedPieceProblemIds.joinToString(", ")
        }
    }

    companion object {
        fun of(
            studentId: String,
            piece: Piece,
        ): StudentPiece {
           return StudentPiece(
               studentId = UUID.fromString(studentId),
               piece = piece
           )
        }
    }

}