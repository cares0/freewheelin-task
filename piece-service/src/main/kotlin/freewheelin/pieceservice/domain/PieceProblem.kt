package freewheelin.pieceservice.domain

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

) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piece_problem_id", insertable = false, updatable = false)
    val id: Long = 0

    companion object {

        fun of(
            piece: Piece,
            problem: Problem,
        ): PieceProblem {
            return PieceProblem(
                piece = piece,
                problem = problem,
            )
        }

    }

}