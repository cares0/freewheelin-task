package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.domain.PieceProblem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PieceProblemJpaRepository : JpaRepository<PieceProblem, Long> {

    @Query("""
        select pp from PieceProblem pp
        join fetch pp.problem
        where pp.id in :pieceProblemIds
    """)
    fun findBatchByIdFetchWithProblemOrNull(
        @Param("pieceProblemIds") pieceProblemIds: Set<Long>,
    ): List<PieceProblem>

}