package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.application.port.outbound.PieceProblemLoadPort
import freewheelin.pieceservice.domain.PieceProblem
import org.springframework.stereotype.Repository

@Repository
class PieceProblemJpaAdapter(
    private val pieceProblemJpaRepository: PieceProblemJpaRepository,
) : PieceProblemLoadPort {

    override fun loadBatchWithProblemById(pieceProblemIds: Set<Long>): List<PieceProblem> {
        return pieceProblemJpaRepository.findBatchByIdFetchWithProblemOrNull(pieceProblemIds)
    }

}