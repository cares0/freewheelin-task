package freewheelin.pieceservice.application.dto

data class CreatePieceCommand(
    val makerId: String,
    val name: String,
    val problemIdsToAdd: Set<Long>
)