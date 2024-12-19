package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.ProblemType
import freewheelin.pieceservice.application.dto.ProblemQueryResult

interface ProblemQueryPort {

    fun queryByUnitCodesAndTypeAndRangeWithLimit(
        unitCodes: List<String>,
        problemType: ProblemType?,
        levelRange: IntRange,
        limit: Int
    ): List<ProblemQueryResult>

}