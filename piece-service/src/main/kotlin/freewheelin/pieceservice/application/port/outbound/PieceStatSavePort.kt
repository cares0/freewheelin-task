package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.model.PieceStat

interface PieceStatSavePort {

    fun save(pieceStat: PieceStat): Long

    fun update(pieceStat: PieceStat)

}