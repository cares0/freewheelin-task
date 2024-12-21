package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.Piece

interface PieceLoadPort {

    fun loadById(pieceId: Long): Piece

}