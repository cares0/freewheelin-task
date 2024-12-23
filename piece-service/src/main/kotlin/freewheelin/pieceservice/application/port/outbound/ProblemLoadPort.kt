package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.model.Problem

interface ProblemLoadPort {

    fun loadBatchById(problemIds: Set<Long>): List<Problem>

}