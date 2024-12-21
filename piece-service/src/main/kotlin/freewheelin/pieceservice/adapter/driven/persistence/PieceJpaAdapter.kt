package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.application.port.outbound.PieceSavePort
import freewheelin.pieceservice.domain.Piece
import org.springframework.stereotype.Repository

@Repository
class PieceJpaAdapter(
    private val pieceJpaRepository: PieceJpaRepository,
) : PieceSavePort {

    override fun save(piece: Piece): Long {
        return pieceJpaRepository.save(piece).id
    }

}