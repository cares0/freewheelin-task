package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.AnalyzePieceResult
import freewheelin.pieceservice.application.dto.ApplyGradeResultToStatCommand
import freewheelin.pieceservice.application.dto.InitStudentStatCommand
import freewheelin.pieceservice.application.port.inbound.AnalyzePieceUseCase
import freewheelin.pieceservice.application.port.inbound.CreatePieceStatUseCase
import freewheelin.pieceservice.application.port.inbound.ApplyGradeResultToStatUseCase
import freewheelin.pieceservice.application.port.inbound.InitStudentStatUseCase
import freewheelin.pieceservice.application.port.outbound.*
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

        val gradedPieceProblems = pieceProblemLoadPort.loadBatchWithProblemById(
            pieceProblemIds = command.gradedPieceProblemIdAndResults.map { it.pieceProblemId }.toSet()
        )

        val pieceProblemIdToGradeResultMap = command.gradedPieceProblemIdAndResults
            .associateBy { it.pieceProblemId }

        pieceStat.applyGradeResult(
            gradedStudentPiece = gradedStudentPiece,
            pieceProblemToGradeResultMap = gradedPieceProblems.associateWith { pieceProblem ->
                pieceProblemIdToGradeResultMap[pieceProblem.id]!!.result
            }
        )

        pieceStatSavePort.update(pieceStat)
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