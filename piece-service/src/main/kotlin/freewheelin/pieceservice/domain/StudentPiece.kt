package freewheelin.pieceservice.domain

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

) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_piece_id", insertable = false, updatable = false)
    val id: Long = 0

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