package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.ProblemQueryCondition
import freewheelin.pieceservice.application.dto.ProblemQueryResult

interface GetProblemByConditionUseCase {

    fun queryByCondition(condition: ProblemQueryCondition): List<ProblemQueryResult>

}