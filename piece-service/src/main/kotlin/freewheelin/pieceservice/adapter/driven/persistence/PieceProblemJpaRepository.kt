package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.domain.model.PieceProblem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PieceProblemJpaRepository : JpaRepository<PieceProblem, Long> {

    @Query("""
        select pp from PieceProblem pp
        join fetch pp.problem
        where pp.id in :pieceProblemIds
    """)
    fun findBatchByIdFetchWithProblem(
        @Param("pieceProblemIds") pieceProblemIds: Set<Long>,
    ): List<PieceProblem>

    @Query("""
        select pp from PieceProblem pp
        where pp.id in :pieceProblemIds
    """)
    fun findBatchById(
        @Param("pieceProblemIds") pieceProblemIds: Set<Long>,
    ): List<PieceProblem>

}