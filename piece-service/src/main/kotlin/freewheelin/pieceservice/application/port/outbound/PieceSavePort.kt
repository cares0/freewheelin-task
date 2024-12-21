package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.Piece

interface PieceSavePort {

    fun save(piece: Piece): Long

}