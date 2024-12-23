package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.model.Piece

interface PieceSavePort {

    fun save(piece: Piece): Long

}