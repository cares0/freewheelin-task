package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.application.port.outbound.ProblemLoadPort
import freewheelin.pieceservice.domain.model.Problem
import org.springframework.stereotype.Repository

@Repository
class ProblemJpaAdapter(
    private val problemJpaRepository: ProblemJpaRepository,
) : ProblemLoadPort {

    override fun loadBatchById(problemIds: Set<Long>): List<Problem> {
        return problemJpaRepository.findBatchById(problemIds)
    }

}