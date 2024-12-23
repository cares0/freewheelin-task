package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.domain.model.PieceStat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PieceStatJpaRepository : JpaRepository<PieceStatEntity, Long> {

    @Query("""
        select ps from PieceStatEntity ps
        join fetch ps.piece
        where ps.id = :id
    """)
    fun findByIdWithPiece(@Param("id") id: Long): PieceStatEntity?

}