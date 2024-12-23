package freewheelin.pieceservice.application.port.outbound

import freewheelin.common.event.Event

interface PieceEventPublishPort {

    fun publish(event: Event)

}