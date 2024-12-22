package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.PieceProblem

interface PieceProblemLoadPort {

    fun loadBatchWithProblemById(pieceProblemIds: Set<Long>): List<PieceProblem>

}