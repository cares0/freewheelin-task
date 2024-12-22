package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.domain.StudentPiece
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface StudentPieceJpaRepository : JpaRepository<StudentPiece, Long> {

    @Query("""
        select sp from StudentPiece sp
        where sp.piece.id = :pieceId
        and sp.studentId = :studentId
    """)
    fun findByPieceIdAndStudentIdOrNull(
        @Param("pieceId") pieceId: Long,
        @Param("studentId") studentId: UUID
    ): StudentPiece?

}