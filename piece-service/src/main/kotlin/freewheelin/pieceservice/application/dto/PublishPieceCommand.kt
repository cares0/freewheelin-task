package freewheelin.pieceservice.application.dto

data class PublishPieceCommand(
    val pieceId: Long,
    val studentIds: Set<String>
)