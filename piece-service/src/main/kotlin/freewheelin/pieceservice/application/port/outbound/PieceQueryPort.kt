package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.application.dto.PieceWithProblemQueryResult

interface PieceQueryPort {

    fun queryWithProblemByPieceId(pieceId: Long): PieceWithProblemQueryResult

}