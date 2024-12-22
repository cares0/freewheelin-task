package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.CreatePieceCommand
import freewheelin.pieceservice.application.dto.GradePieceForStudentBatchCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult
import freewheelin.pieceservice.application.dto.PublishPieceCommand
import freewheelin.pieceservice.application.port.inbound.CreatePieceUseCase
import freewheelin.pieceservice.application.port.inbound.GradePieceForStudentUseCase
import freewheelin.pieceservice.application.port.inbound.PublishPieceUseCase
import freewheelin.pieceservice.application.port.outbound.*
import freewheelin.pieceservice.domain.Piece
import org.springframework.stereotype.Service

@Service
class PieceService(
    private val problemLoadPort: ProblemLoadPort,
    private val pieceSavePort: PieceSavePort,
    private val pieceLoadPort: PieceLoadPort,
    private val studentPieceLoadPort: StudentPieceLoadPort,
    private val pieceProblemLoadPort: PieceProblemLoadPort,
) : CreatePieceUseCase,
    PublishPieceUseCase,
    GradePieceForStudentUseCase
{

    override fun create(command: CreatePieceCommand): Long {
        val newPiece = Piece.of(
            name = command.name,
            makerId = command.makerId
        )

        val problemsToAdd = problemLoadPort.loadBatchById(command.problemIdsToAdd)
        newPiece.addProblems(problemsToAdd)

        val savedId = pieceSavePort.save(newPiece)
        return savedId
    }

    override fun publish(pieceId: Long, command: PublishPieceCommand) {
        val piece = pieceLoadPort.loadById(pieceId)
        piece.publishBatch(command.studentIds)
    }

    override fun gradeBatch(
        pieceId: Long,
        command: GradePieceForStudentBatchCommand
    ): List<PieceProblemIdAndResult> {
        val studentPiece = studentPieceLoadPort.loadByPieceIdAndStudentId(pieceId, command.studentId)

        val submittedAnswerMap = command.problemIdAndAnswers.associate { it.pieceProblemId to it.answer }

        val pieceProblemsToGrade = pieceProblemLoadPort.loadBatchWithProblemById(submittedAnswerMap.keys)

        val gradeResultMap = pieceProblemsToGrade.associate { pieceProblem ->
            val submittedAnswer = submittedAnswerMap[pieceProblem.id]!!

            val isSolved = studentPiece.grade(pieceProblem, submittedAnswer)

            pieceProblem.id to isSolved
        }

        return gradeResultMap.map {
            PieceProblemIdAndResult(
                pieceProblemId = it.key,
                isSolved = it.value
            )
        }
    }

}