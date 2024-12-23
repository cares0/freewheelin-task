package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.domain.model.Piece
import freewheelin.pieceservice.supports.EntityBase
import jakarta.persistence.*

@Entity
@Table(name = "piece_stat")
class PieceStatEntity(

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "piece_id")
    val piece: Piece,

    @Lob
    @Column(columnDefinition="blob")
    var studentStatsContainer: ByteArray,

    @Lob
    @Column(columnDefinition="blob")
    var problemStatsContainer: ByteArray,

) : EntityBase() {

    @Id
    @Column(insertable = false, updatable = false)
    val id: Long = 0

}