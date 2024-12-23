package freewheelin.pieceservice.adapter.driving.web.response

import freewheelin.common.mapper.Mapper
import freewheelin.pieceservice.application.dto.AnalyzePieceResult
import org.springframework.stereotype.Component
import kotlin.math.round
import kotlin.reflect.KClass

@Component
class AnalyzePieceResponseMapper : Mapper<AnalyzePieceResult, AnalyzePieceResponse> {
    override val sourceType: KClass<AnalyzePieceResult> = AnalyzePieceResult::class
    override val targetType: KClass<AnalyzePieceResponse> = AnalyzePieceResponse::class

    override fun map(source: AnalyzePieceResult): AnalyzePieceResponse {
        return AnalyzePieceResponse(
            pieceId = source.pieceId,
            pieceName = source.pieceName,
            publishedStudentIds = source.studentResults.map { it.studentId }.toSet(),
            studentStats = source.studentResults.map { studentResult ->
                AnalyzePieceResponse.StudentStatResponse(
                    studentId = studentResult.studentId,
                    solvedPercentage = round(studentResult.solvedRate * 100).toInt(),
                )
            },
            problemStats = source.problemResults.map { problemResult ->
                AnalyzePieceResponse.ProblemStatResponse(
                    problemId = problemResult.problemId,
                    number = problemResult.number,
                    solvedStudentPercentage = round(problemResult.solvedRate * 100).toInt(),
                )
            },
        )
    }
}