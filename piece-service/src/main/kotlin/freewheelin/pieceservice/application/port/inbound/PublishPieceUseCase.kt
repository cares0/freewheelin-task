package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.PublishPieceCommand

interface PublishPieceUseCase {

    fun publish(pieceId: Long, command: PublishPieceCommand)

}