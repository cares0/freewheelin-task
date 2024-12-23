package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.common.supports.EntityNotExistException
import freewheelin.common.supports.findByIdOrThrow
import freewheelin.pieceservice.application.port.outbound.StudentPieceLoadPort
import freewheelin.pieceservice.domain.model.StudentPiece
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class StudentPieceJpaAdapter(
    private val studentPieceJpaRepository: StudentPieceJpaRepository,
) : StudentPieceLoadPort {

    override fun loadById(studentPieceId: Long): StudentPiece {
        return studentPieceJpaRepository.findByIdOrThrow(studentPieceId)
    }

    override fun loadAllByPieceIdAndStudentIds(pieceId: Long, studentIds: Set<UUID>): List<StudentPiece> {
        return studentPieceJpaRepository.findAllByPieceIdAndStudentIds(pieceId, studentIds)
    }

    override fun loadByPieceIdAndStudentId(pieceId: Long, studentId: UUID): StudentPiece {
        return studentPieceJpaRepository.findByPieceIdAndStudentIdOrNull(pieceId, studentId)
            ?: throw EntityNotExistException(StudentPiece::class, "pieceId: $pieceId-studentId: $studentId")
    }

}