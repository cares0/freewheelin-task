package freewheelin.pieceservice.adapter.driven.persistence

import freewheelin.pieceservice.application.port.outbound.PieceProblemLoadPort
import freewheelin.pieceservice.domain.model.PieceProblem
import org.springframework.stereotype.Repository

@Repository
class PieceProblemJpaAdapter(
    private val pieceProblemJpaRepository: PieceProblemJpaRepository,
) : PieceProblemLoadPort {

    override fun loadBatchWithProblemById(pieceProblemIds: Set<Long>): List<PieceProblem> {
        return pieceProblemJpaRepository.findBatchByIdFetchWithProblem(pieceProblemIds)
    }

    override fun loadBatchById(pieceProblemIds: Set<Long>): List<PieceProblem> {
        return pieceProblemJpaRepository.findBatchById(pieceProblemIds)
    }

}