package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.model.PieceStat

interface PieceStatLoadPort {

    fun loadByPieceIdWithPiece(pieceId: Long): PieceStat

}