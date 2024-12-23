package freewheelin.pieceservice.adapter.driven.persistence

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import freewheelin.common.supports.EntityNotExistException
import freewheelin.common.supports.findByIdOrThrow
import freewheelin.pieceservice.application.port.outbound.PieceStatLoadPort
import freewheelin.pieceservice.application.port.outbound.PieceStatSavePort
import freewheelin.pieceservice.domain.model.PieceProblemStat
import freewheelin.pieceservice.domain.model.PieceStat
import freewheelin.pieceservice.domain.model.StudentPieceStat
import org.springframework.stereotype.Repository

@Repository
class PieceStatJpaAdapter(
    private val pieceStatJpaRepository: PieceStatJpaRepository,
    private val objectMapper: ObjectMapper,
) : PieceStatSavePort,
    PieceStatLoadPort
{

    override fun save(pieceStat: PieceStat): Long {
        val entity = PieceStatEntity(
            piece = pieceStat.piece,
            studentStatsContainer = objectMapper.writeValueAsBytes(pieceStat.studentStats),
            problemStatsContainer = objectMapper.writeValueAsBytes(pieceStat.problemStats),
        )

        return pieceStatJpaRepository.save(entity).id
    }

    override fun update(pieceStat: PieceStat) {
        val pieceStatEntity = pieceStatJpaRepository.findByIdOrThrow(pieceStat.id)
        pieceStatEntity.studentStatsContainer = objectMapper.writeValueAsBytes(pieceStat.studentStats)
        pieceStatEntity.problemStatsContainer = objectMapper.writeValueAsBytes(pieceStat.problemStats)
    }

    override fun loadByPieceIdWithPiece(pieceId: Long): PieceStat {
        val entity = pieceStatJpaRepository.findByIdWithPiece(pieceId)
            ?: throw EntityNotExistException(PieceStat::class, pieceId.toString())

        return PieceStat(
            piece = entity.piece,
            studentStats = objectMapper
                .readValue(entity.studentStatsContainer, object : TypeReference<MutableList<StudentPieceStat>>() {}),
            problemStats = objectMapper
                .readValue(entity.problemStatsContainer, object : TypeReference<MutableList<PieceProblemStat>>() {}),
        )
    }

}