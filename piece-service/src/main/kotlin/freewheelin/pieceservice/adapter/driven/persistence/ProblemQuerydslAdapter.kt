package freewheelin.pieceservice.adapter.driven.persistence

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import freewheelin.common.supports.nullSafeBuilder
import freewheelin.pieceservice.domain.ProblemType
import freewheelin.pieceservice.application.dto.ProblemQueryResult
import freewheelin.pieceservice.application.port.outbound.ProblemQueryPort
import freewheelin.pieceservice.domain.QProblem.problem
import org.springframework.stereotype.Repository

@Repository
class ProblemQuerydslAdapter(
    private val queryFactory: JPAQueryFactory
) : ProblemQueryPort {

    override fun queryByUnitCodesAndTypeAndRangeWithLimit(
        unitCodes: List<String>,
        problemType: ProblemType?,
        levelRange: IntRange,
        limit: Int
    ): List<ProblemQueryResult> {
        return queryFactory
            .select(
                Projections.fields(
                    ProblemQueryResult::class.java,
                    problem.id,
                    problem.answer,
                    problem.unit.code.`as`("unitCode"),
                    problem.level,
                    problem.type.`as`("problemType"),
                )
            )
            .from(problem)
            .where(
                problem.unit.code.`in`(unitCodes),
                nullSafeBuilder { problem.type.eq(problemType) },
                problem.level.`in`(levelRange.toList())
            )
            .limit(limit.toLong())
            .fetch()
    }

}