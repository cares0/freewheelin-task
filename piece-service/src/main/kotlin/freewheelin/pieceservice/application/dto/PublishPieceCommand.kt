package freewheelin.pieceservice.application.dto

data class PublishPieceCommand(
    val studentIds: Set<String>
)