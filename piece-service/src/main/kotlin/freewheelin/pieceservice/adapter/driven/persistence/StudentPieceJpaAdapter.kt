package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.common.supports.EntityNotExistException
import freewheelin.pieceservice.application.port.outbound.StudentPieceLoadPort
import freewheelin.pieceservice.domain.StudentPiece
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class StudentPieceJpaAdapter(
    private val studentPieceJpaRepository: StudentPieceJpaRepository,
) : StudentPieceLoadPort {

    override fun loadByPieceIdAndStudentId(pieceId: Long, studentId: UUID): StudentPiece {
        return studentPieceJpaRepository.findByPieceIdAndStudentIdOrNull(pieceId, studentId)
            ?: throw EntityNotExistException(StudentPiece::class, "pieceId: $pieceId-studentId: $studentId")
    }

}