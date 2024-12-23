package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.AnalyzePieceResult

interface AnalyzePieceUseCase {

    fun analyze(pieceId: Long): AnalyzePieceResult

}