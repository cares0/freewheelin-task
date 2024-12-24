package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.AnalyzePieceResult
import freewheelin.pieceservice.application.dto.ApplyGradeResultToStatCommand
import freewheelin.pieceservice.application.dto.InitStudentStatCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult
import freewheelin.pieceservice.application.port.inbound.AnalyzePieceUseCase
import freewheelin.pieceservice.application.port.inbound.CreatePieceStatUseCase
import freewheelin.pieceservice.application.port.inbound.ApplyGradeResultToStatUseCase
import freewheelin.pieceservice.application.port.inbound.InitStudentStatUseCase
import freewheelin.pieceservice.application.port.outbound.*
import freewheelin.pieceservice.domain.model.GradeResult
import freewheelin.pieceservice.domain.model.PieceProblem
import freewheelin.pieceservice.domain.model.PieceStat
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PieceStatService(
    val pieceStatSavePort: PieceStatSavePort,
    val pieceStatLoadPort: PieceStatLoadPort,
    val pieceLoadPort: PieceLoadPort,
    val studentPieceLoadPort: StudentPieceLoadPort,
    val pieceProblemLoadPort: PieceProblemLoadPort,
) : CreatePieceStatUseCase,
    InitStudentStatUseCase,
    ApplyGradeResultToStatUseCase,
    AnalyzePieceUseCase
{

    override fun create(pieceId: Long): Long {
        val createdPiece = pieceLoadPort.loadById(pieceId)

        val newStats = PieceStat.of(createdPiece)

        val savedId = pieceStatSavePort.save(newStats)

        return savedId
    }

    override fun initStudentStat(command: InitStudentStatCommand) {
        val pieceStat = pieceStatLoadPort.loadByPieceIdWithPiece(command.pieceId)

        val studentPieceToInit = studentPieceLoadPort
            .loadAllByPieceIdAndStudentIds(command.pieceId, command.studentIds)

        pieceStat.initStudentStats(studentPieceToInit)

        pieceStatSavePort.update(pieceStat)
    }

    override fun applyResult(command: ApplyGradeResultToStatCommand) {
        val gradedStudentPiece = studentPieceLoadPort.loadById(command.gradedStudentPieceId)

        val pieceStat = pieceStatLoadPort.loadByPieceIdWithPiece(gradedStudentPiece.piece.id)

        val gradeResultPerPieceProblem = resolveGradeResultPerPieceProblem(command.gradedPieceProblemIdAndResults)

        pieceStat.applyGradeResult(
            gradedStudentPiece = gradedStudentPiece,
            gradeResultPerPieceProblem = gradeResultPerPieceProblem
        )

        pieceStatSavePort.update(pieceStat)
    }

    private fun resolveGradeResultPerPieceProblem(
        pieceProblemIdAndResults: List<PieceProblemIdAndResult>
    ): Map<PieceProblem, GradeResult> {
        val gradeResultPerPieceProblemId = pieceProblemIdAndResults
            .associateBy { it.pieceProblemId }

        val gradeResultPerPieceProblem = pieceProblemLoadPort.loadBatchWithProblemById(
            pieceProblemIds = gradeResultPerPieceProblemId.keys
        ).associateWith { gradeResultPerPieceProblemId[it.id]!!.result }

        return gradeResultPerPieceProblem
    }

    override fun analyze(pieceId: Long): AnalyzePieceResult {
        val pieceStat = pieceStatLoadPort.loadByPieceIdWithPiece(pieceId)

        return AnalyzePieceResult(
            pieceId = pieceStat.piece.id,
            pieceName = pieceStat.piece.name,
            studentResults = pieceStat.studentStats.map {
                AnalyzePieceResult.StudentAnalyzeResult(
                    studentId = it.studentId.toString(),
                    solvedRate = it.solvedProblemRate,
                )
            },
            problemResults = pieceStat.problemStats.map {
                AnalyzePieceResult.ProblemAnalyzeResult(
                    problemId = it.problemId,
                    number = it.number,
                    solvedRate = it.solvedStudentRate,
                )
            },
        )
    }

}