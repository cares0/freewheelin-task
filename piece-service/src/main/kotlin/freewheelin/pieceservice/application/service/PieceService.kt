package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.*
import freewheelin.pieceservice.application.port.inbound.CreatePieceUseCase
import freewheelin.pieceservice.application.port.inbound.GradePieceUseCase
import freewheelin.pieceservice.application.port.inbound.PublishPieceUseCase
import freewheelin.pieceservice.application.port.outbound.*
import freewheelin.pieceservice.domain.event.PieceCreatedEvent
import freewheelin.pieceservice.domain.event.PieceGradedEvent
import freewheelin.pieceservice.domain.event.PiecePublishedEvent
import freewheelin.pieceservice.domain.model.GradeResult
import freewheelin.pieceservice.domain.model.Piece
import freewheelin.pieceservice.domain.model.PieceProblem
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
    GradePieceUseCase
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

    override fun gradeBatch(
        pieceId: Long,
        command: GradePieceBatchCommand
    ): List<PieceProblemIdAndResult> {
        // 학생의 학습지를 조회합니다.
        val studentPiece = studentPieceLoadPort.loadByPieceIdAndStudentId(pieceId, command.studentId)

        // 채점해야 할 학습지의 문제들을 조회합니다.
        val pieceProblemMap = getPieceProblemMap(command.problemIdAndAnswers)

        // 제출한 답안을 학습지 문제와 연관짓습니다.
        val submittedAnswerPerPieceProblem = associateProblemToAnswer(pieceProblemMap, command.problemIdAndAnswers)

        // 학습지 문제를 채점합니다.
        val gradeResultPerPieceProblem = studentPiece.gradeBatch(submittedAnswerPerPieceProblem)

        // 채점 결과를 학습지 문제 ID와 연관짓습니다.
        val gradeResultPerPieceProblemId = convertResultPerPieceProblemId(gradeResultPerPieceProblem)

        pieceEventPublishPort.publish(
            PieceGradedEvent(
                gradedStudentPieceId = studentPiece.id,
                gradedPieceProblemIdAndResults = gradeResultPerPieceProblemId,
            )
        )

        return gradeResultPerPieceProblemId
    }

    private fun convertResultPerPieceProblemId(gradeResultPerPieceProblem: Map<PieceProblem, GradeResult>) =
        gradeResultPerPieceProblem.map {
            PieceProblemIdAndResult(
                pieceProblemId = it.key.id,
                result = it.value
            )
        }

    private fun associateProblemToAnswer(
        pieceProblemMap: Map<Long, PieceProblem>,
        pieceProblemIdAndAnswers: List<PieceProblemIdAndAnswer>
    ) = pieceProblemIdAndAnswers.associate {
        pieceProblemMap[it.pieceProblemId]!! to it.answer
    }

    private fun getPieceProblemMap(pieceProblemIdAndAnswers: List<PieceProblemIdAndAnswer>) =
        pieceProblemLoadPort
            .loadBatchWithProblemById(pieceProblemIdAndAnswers.map { it.pieceProblemId }.toSet())
            .associateBy { it.id }

}