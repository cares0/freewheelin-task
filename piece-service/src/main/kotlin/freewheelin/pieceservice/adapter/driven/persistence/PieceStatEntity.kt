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

    /**
     * Hibernate6 부터 @JdbcTypeCode(SqlTypes.JSON)으로 JSON 컬럼 매핑이 가능하나,
     * 완벽하게 JPA의 기능을 지원하지 않아 ByteArray 사용으로 우회 (Collection 타입 사용 시 변경 감지가 제한적인 것 등)
     * **/

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