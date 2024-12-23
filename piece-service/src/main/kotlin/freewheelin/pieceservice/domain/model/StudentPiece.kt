package freewheelin.pieceservice.domain.model

import freewheelin.pieceservice.domain.exception.CannotGradePieceException
import freewheelin.pieceservice.supports.EntityBase
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

    fun grade(
        pieceProblem: PieceProblem,
        submittedAnswer: String,
    ): GradeResult {
        validateProblemIsIncludedInPiece(pieceProblem)

        return if (pieceProblem.problem.answer == submittedAnswer) GradeResult.SOLVED
        else GradeResult.FAILED
    }

    private fun validateProblemIsIncludedInPiece(pieceProblem: PieceProblem) {
        if (pieceProblem.piece != this.piece) throw CannotGradePieceException()
    }

    companion object {
        fun of(
            studentId: UUID,
            piece: Piece,
        ): StudentPiece {
           return StudentPiece(
               studentId = studentId,
               piece = piece
           )
        }
    }

}