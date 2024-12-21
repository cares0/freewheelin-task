package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.PieceWithProblemQueryResult

interface GetPieceWithProblemUseCase {

    fun queryWithProblemById(pieceId: Long): PieceWithProblemQueryResult

}