package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.domain.Piece
import org.springframework.data.jpa.repository.JpaRepository

interface PieceJpaRepository : JpaRepository<Piece, Long> {
}