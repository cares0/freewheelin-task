package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.ApplyGradeResultToStatCommand

interface ApplyGradeResultToStatUseCase {

    fun applyResult(command: ApplyGradeResultToStatCommand)

}