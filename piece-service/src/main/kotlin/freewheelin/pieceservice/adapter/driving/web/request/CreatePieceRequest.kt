package freewheelin.pieceservice.adapter.driving.web.request

data class CreatePieceRequest(
    val makerId: String,
    val name: String,
    val problemIdsToAdd: Set<Long>,
)