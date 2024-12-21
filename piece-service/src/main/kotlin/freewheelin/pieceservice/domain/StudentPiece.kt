package freewheelin.pieceservice.domain

import jakarta.persistence.*
import jakarta.persistence.FetchType.*
import jakarta.persistence.GenerationType.*

@Entity
class StudentPiece(

    @Column(columnDefinition = "BINARY(16)")
    val studentId: String,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "piece_id")
    val piece: Piece,

) {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "student_piece_id", insertable = false, updatable = false)
    val id: Long = 0

}