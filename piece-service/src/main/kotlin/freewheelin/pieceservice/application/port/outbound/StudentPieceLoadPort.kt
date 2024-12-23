package freewheelin.pieceservice.application.port.outbound

import freewheelin.pieceservice.domain.model.StudentPiece
import java.util.UUID

interface StudentPieceLoadPort {

    fun loadById(studentPieceId: Long): StudentPiece

    fun loadAllByPieceIdAndStudentIds(pieceId: Long, studentIds: Set<UUID>): List<StudentPiece>

    fun loadByPieceIdAndStudentId(pieceId: Long, studentId: UUID): StudentPiece

}