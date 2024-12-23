package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.model.PieceProblem

interface PieceProblemLoadPort {

    fun loadBatchWithProblemById(pieceProblemIds: Set<Long>): List<PieceProblem>

    fun loadBatchById(pieceProblemIds: Set<Long>): List<PieceProblem>

}