package freewheelin.pieceservice.application.dto

import freewheelin.pieceservice.domain.ProblemLevelCategory
import freewheelin.pieceservice.domain.ProblemType

data class ProblemQueryCondition(
    val totalCount: Int,
    val unitCodeList: List<String>,
    val levelCategory: ProblemLevelCategory,
    val type: ProblemType?,
)