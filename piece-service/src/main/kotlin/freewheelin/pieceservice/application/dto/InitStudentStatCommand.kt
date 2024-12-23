package freewheelin.pieceservice.application.dto

import java.util.UUID

data class InitStudentStatCommand(
    val pieceId: Long,
    val studentIds: Set<UUID>
)