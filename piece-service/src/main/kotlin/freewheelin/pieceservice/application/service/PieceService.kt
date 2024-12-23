package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.CreatePieceCommand
import freewheelin.pieceservice.application.dto.GradePieceForStudentCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult
import freewheelin.pieceservice.application.dto.PublishPieceCommand
import freewheelin.pieceservice.application.port.inbound.CreatePieceUseCase
import freewheelin.pieceservice.application.port.inbound.GradePieceForStudentUseCase
import freewheelin.pieceservice.application.port.inbound.PublishPieceUseCase
import freewheelin.pieceservice.application.port.outbound.*
import freewheelin.pieceservice.domain.event.PieceCreatedEvent
import freewheelin.pieceservice.domain.event.PieceGradedEvent
import freewheelin.pieceservice.domain.event.PiecePublishedEvent
import freewheelin.pieceservice.domain.model.Piece
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PieceService(
    private val problemLoadPort: ProblemLoadPort,
    private val pieceSavePort: PieceSavePort,
    private val pieceLoadPort: PieceLoadPort,
    private val studentPieceLoadPort: StudentPieceLoadPort,
    private val pieceProblemLoadPort: PieceProblemLoadPort,
    private val pieceEventPublishPort: PieceEventPublishPort,
) : CreatePieceUseCase,
    PublishPieceUseCase,
    GradePieceForStudentUseCase
{

    override fun create(command: CreatePieceCommand): Long {
        val newPiece = createNewPiece(command)

        addProblemsToPiece(command.problemIdsToAdd, newPiece)

        val savedId = pieceSavePort.save(newPiece)

        pieceEventPublishPort.publish(PieceCreatedEvent(savedId))
        return savedId
    }

    private fun createNewPiece(command: CreatePieceCommand) = Piece.of(
        name = command.name,
        makerId = command.makerId
    )

    private fun addProblemsToPiece(
        problemIds: Set<Long>,
        newPiece: Piece,
    ) {
        val problemsToAdd = problemLoadPort.loadBatchById(problemIds)
        newPiece.addProblems(problemsToAdd)
    }

    override fun publish(pieceId: Long, command: PublishPieceCommand) {
        val piece = pieceLoadPort.loadById(pieceId)

        val publishedStudentIds = piece.publishBatch(command.studentIds)

        pieceEventPublishPort.publish(
            PiecePublishedEvent(
                publishedPieceId = piece.id,
                publishedStudentIds = publishedStudentIds,
            )
        )
    }

    override fun grade(
        pieceId: Long,
        command: GradePieceForStudentCommand
    ): List<PieceProblemIdAndResult> {
        val studentPiece = studentPieceLoadPort.loadByPieceIdAndStudentId(pieceId, command.studentId)

        val submittedAnswerMap = command.problemIdAndAnswers.associate { it.pieceProblemId to it.answer }

        val pieceProblemsToGrade = pieceProblemLoadPort.loadBatchWithProblemById(submittedAnswerMap.keys)

        val gradeResultMap = pieceProblemsToGrade.associate { pieceProblem ->
            val submittedAnswer = submittedAnswerMap[pieceProblem.id]!!

            val gradeResult = studentPiece.grade(pieceProblem, submittedAnswer)

            pieceProblem.id to gradeResult
        }

        val results = gradeResultMap.map {
            PieceProblemIdAndResult(
                pieceProblemId = it.key,
                result = it.value
            )
        }

        pieceEventPublishPort.publish(
            PieceGradedEvent(
                gradedStudentPieceId = studentPiece.id,
                gradedPieceProblemIdAndResults = results,
            )
        )

        return results
    }

}