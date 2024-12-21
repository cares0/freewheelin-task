package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.common.supports.findByIdOrThrow
import freewheelin.pieceservice.application.port.outbound.PieceLoadPort
import freewheelin.pieceservice.application.port.outbound.PieceSavePort
import freewheelin.pieceservice.domain.Piece
import org.springframework.stereotype.Repository

@Repository
class PieceJpaAdapter(
    private val pieceJpaRepository: PieceJpaRepository,
) : PieceSavePort,
    PieceLoadPort
{

    override fun save(piece: Piece): Long {
        return pieceJpaRepository.save(piece).id
    }

    override fun loadById(pieceId: Long): Piece {
        return pieceJpaRepository.findByIdOrThrow(pieceId)
    }

}