package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.CreatePieceCommand

interface CreatePieceUseCase {

    fun create(command: CreatePieceCommand): Long

}