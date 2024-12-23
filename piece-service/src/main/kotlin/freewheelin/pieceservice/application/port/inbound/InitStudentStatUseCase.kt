package freewheelin.pieceservice.application.port.inbound

import freewheelin.pieceservice.application.dto.InitStudentStatCommand

interface InitStudentStatUseCase {

    fun initStudentStat(command: InitStudentStatCommand)

}