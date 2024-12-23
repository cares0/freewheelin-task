package freewheelin.pieceservice.application.dto

import java.util.UUID

data class PublishPieceCommand(
    val studentIds: Set<UUID>
)