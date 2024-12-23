package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.model.Piece

interface PieceLoadPort {

    fun loadById(pieceId: Long): Piece

}