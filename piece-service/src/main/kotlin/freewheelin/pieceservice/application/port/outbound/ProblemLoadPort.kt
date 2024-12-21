package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.Problem

interface ProblemLoadPort {

    fun loadBatchById(problemIds: Set<Long>): List<Problem>

}