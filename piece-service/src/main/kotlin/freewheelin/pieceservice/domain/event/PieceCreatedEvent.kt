package freewheelin.pieceservice.domain.event

import freewheelin.common.event.Event

data class PieceCreatedEvent(
    val createdPieceId: Long,
) : Event()