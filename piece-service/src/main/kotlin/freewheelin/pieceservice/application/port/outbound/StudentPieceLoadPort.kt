package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.StudentPiece
import java.util.UUID

interface StudentPieceLoadPort {

    fun loadByPieceIdAndStudentId(pieceId: Long, studentId: UUID): StudentPiece

}