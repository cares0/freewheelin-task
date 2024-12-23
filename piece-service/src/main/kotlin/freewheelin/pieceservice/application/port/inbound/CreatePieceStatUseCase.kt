package freewheelin.pieceservice.application.port.inbound

interface CreatePieceStatUseCase {

    fun create(pieceId: Long): Long

}