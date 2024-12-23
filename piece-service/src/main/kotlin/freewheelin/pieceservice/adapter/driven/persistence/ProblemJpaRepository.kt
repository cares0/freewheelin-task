package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.domain.model.Problem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProblemJpaRepository : JpaRepository<Problem, Long> {

    @Query("""
        select p from Problem p
        where p.id in :problemIds
    """)
    fun findBatchById(@Param("problemIds") problemIds: Set<Long>): List<Problem>

}