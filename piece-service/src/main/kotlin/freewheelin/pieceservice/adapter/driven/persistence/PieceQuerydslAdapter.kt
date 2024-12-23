package freewheelin.pieceservice.adapter.driven.persistence

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import freewheelin.common.supports.EntityNotExistException
import freewheelin.pieceservice.application.dto.PieceProblemQueryResult
import freewheelin.pieceservice.application.dto.PieceWithProblemQueryResult
import freewheelin.pieceservice.application.port.outbound.PieceQueryPort
import freewheelin.pieceservice.domain.model.Piece
import freewheelin.pieceservice.domain.model.QPiece.*
import freewheelin.pieceservice.domain.model.QPieceProblem.*
import freewheelin.pieceservice.domain.model.QProblem.*
import org.springframework.stereotype.Repository

@Repository
class PieceQuerydslAdapter(
    private val queryFactory: JPAQueryFactory,
) : PieceQueryPort {

    override fun queryWithProblemByPieceId(pieceId: Long): PieceWithProblemQueryResult {
        return queryFactory
            .from(piece)
            .join(piece.pieceProblems, pieceProblem)
            .join(pieceProblem.problem, problem)
            .where(piece.id.eq(pieceId))
            .orderBy(pieceProblem.number.desc())
            .transform(
                groupBy(piece.id).list(
                    Projections.fields(
                        PieceWithProblemQueryResult::class.java,
                        piece.id,
                        piece.name,
                        piece.totalProblemCount,
                        list(
                            Projections.fields(
                                PieceProblemQueryResult::class.java,
                                pieceProblem.id,
                                problem.id.`as`("problemId"),
                                pieceProblem.number,
                                problem.contents,
                                problem.unit.code.`as`("unitCode"),
                                problem.level,
                                problem.type,
                                problem.answer,
                            )
                        ).`as`("pieceProblems")
                    )
                )
            ).firstOrNull() ?: throw EntityNotExistException(Piece::class, pieceId.toString())
    }

}