package freewheelin.pieceservice.application.dto

import freewheelin.pieceservice.application.service.ProblemLevelCategory
import freewheelin.pieceservice.domain.ProblemType

data class ProblemQueryCondition(
    val totalCount: Int,
    val unitCodeList: List<String>,
    val levelCategory: ProblemLevelCategory,
    val type: ProblemType?,
)