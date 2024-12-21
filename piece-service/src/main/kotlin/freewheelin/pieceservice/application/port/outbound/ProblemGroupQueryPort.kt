package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.ProblemType

interface ProblemGroupQueryPort {

    fun queryAvailableProblemCountGroupedByLevel(
        unitCodes: List<String>,
        problemType: ProblemType?,
    ): Map<Int, Int>

}