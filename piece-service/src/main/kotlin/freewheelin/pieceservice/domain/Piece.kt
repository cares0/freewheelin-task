package freewheelin.pieceservice.domain

import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import java.util.*

@Entity
class Piece private constructor (

    val name: String,

    @Column(columnDefinition = "BINARY(16)")
    val makerId: UUID,

) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piece_id", insertable = false, updatable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "piece", cascade = [PERSIST, REMOVE])
    val pieceProblems: MutableList<PieceProblem> = mutableListOf()

    @OneToMany(mappedBy = "piece", cascade = [PERSIST, REMOVE])
    val studentPieces: MutableList<StudentPiece> = mutableListOf()

    var totalProblemCount: Int = 0

    fun validateProblemCountBeforeAdd(countToAdd: Int = 1) {
        if (totalProblemCount + countToAdd > MAXIMUM_PROBLEM_COUNT) {
            throw PieceProblemCountExceedException(MAXIMUM_PROBLEM_COUNT)
        }
    }

    fun addProblems(problems: List<Problem>) {
        validateProblemCountBeforeAdd(problems.size)

        this.pieceProblems.addAll(
            problems.mapIndexed { index, problem ->
                PieceProblem.of(
                    piece = this,
                    problem = problem,
                    number = index + 1,
                )
            }
        )

        this.totalProblemCount += problems.size
    }

    fun publishBatch(studentIds: Set<String>) {
        val alreadyPublishedStudentIds = studentPieces.map { it.studentId.toString() }.toSet()
        val newStudentIds = studentIds.subtract(alreadyPublishedStudentIds)

        val newStudentPieces = newStudentIds.map { studentId ->
            StudentPiece.of(
                studentId = studentId,
                piece = this
            )
        }

        this.studentPieces.addAll(newStudentPieces)
    }


    companion object {
        private const val MAXIMUM_PROBLEM_COUNT = 50

        fun of(
            name: String,
            makerId: String,
        ): Piece {
            return Piece(
                name = name,
                makerId = UUID.fromString(makerId)
            )
        }
    }

}