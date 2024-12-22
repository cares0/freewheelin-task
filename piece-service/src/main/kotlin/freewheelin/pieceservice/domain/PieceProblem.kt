package freewheelin.pieceservice.domain

import freewheelin.common.supports.EntityBase
import jakarta.persistence.*
import jakarta.persistence.FetchType.*

@Entity
class PieceProblem private constructor(

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "piece_id")
    val piece: Piece,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "problem_id")
    val problem: Problem,

    val number: Int,

) : EntityBase() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piece_problem_id", insertable = false, updatable = false)
    val id: Long = 0

    private var solvedStudentIdJoined: String? = null
    private var failedStudentIdJoined: String? = null

    val solvedStudentIds: List<String>
        get() = this.solvedStudentIdJoined?.split(", ") ?: emptyList()

    val failedStudentIds: List<String>
        get() = this.failedStudentIdJoined?.split(", ") ?: emptyList()

    fun addSolvedStudent(studentId: String) {
        removeFailedStudent(studentId)
        this.solvedStudentIdJoined = solvedStudentIds.plus(studentId).joinToString(", ")
    }

    fun addFailedStudent(studentId: String) {
        removeSolvedStudent(studentId)
        this.failedStudentIdJoined = failedStudentIds.plus(studentId).joinToString(", ")
    }

    private fun removeSolvedStudent(studentId: String) {
        val removedList = solvedStudentIds.filterNot { it == studentId }

        if (removedList.isNotEmpty()) {
            this.solvedStudentIdJoined = removedList.joinToString(", ")
        }
    }

    private fun removeFailedStudent(studentId: String) {
        val removedList = failedStudentIds.filterNot { it == studentId }

        if (removedList.isNotEmpty()) {
            this.failedStudentIdJoined = removedList.joinToString(", ")
        }
    }

    companion object {

        fun of(
            piece: Piece,
            problem: Problem,
            number: Int,
        ): PieceProblem {
            return PieceProblem(
                piece = piece,
                problem = problem,
                number = number,
            )
        }

    }

}