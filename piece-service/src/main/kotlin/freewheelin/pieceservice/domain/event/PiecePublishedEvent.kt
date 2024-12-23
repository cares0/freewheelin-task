package freewheelin.pieceservice.domain.event

import freewheelin.common.event.Event
import java.util.UUID

data class PiecePublishedEvent(
    val publishedPieceId: Long,
    val publishedStudentIds: Set<UUID>
) : Event()