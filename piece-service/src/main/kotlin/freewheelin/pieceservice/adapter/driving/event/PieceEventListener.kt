package freewheelin.pieceservice.adapter.driving.event

import freewheelin.common.supports.logger
import freewheelin.pieceservice.application.dto.ApplyGradeResultToStatCommand
import freewheelin.pieceservice.application.dto.InitStudentStatCommand
import freewheelin.pieceservice.application.port.inbound.ApplyGradeResultToStatUseCase
import freewheelin.pieceservice.application.port.inbound.CreatePieceStatUseCase
import freewheelin.pieceservice.application.port.inbound.InitStudentStatUseCase
import freewheelin.pieceservice.domain.event.PieceCreatedEvent
import freewheelin.pieceservice.domain.event.PieceGradedEvent
import freewheelin.pieceservice.domain.event.PiecePublishedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PieceEventListener(
    private val createPieceStatUseCase: CreatePieceStatUseCase,
    private val initStudentStatUseCase: InitStudentStatUseCase,
    private val applyGradeResultToStatUseCase: ApplyGradeResultToStatUseCase,
) {

    private val log = logger()

    @Async("asyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun listenPieceCreatedEvent(event: PieceCreatedEvent) {
        log.info("listen event $event")
        createPieceStatUseCase.create(event.createdPieceId)
    }

    @Async("asyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun listenPiecePublishedEvent(event: PiecePublishedEvent) {
        log.info("listen event $event")
        initStudentStatUseCase.initStudentStat(
            InitStudentStatCommand(
                pieceId = event.publishedPieceId,
                studentIds = event.publishedStudentIds,
            )
        )
    }

    @Async("asyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun listenPieceGradedEvent(event: PieceGradedEvent) {
        log.info("listen event $event")
        applyGradeResultToStatUseCase.applyResult(
            ApplyGradeResultToStatCommand(
                gradedStudentPieceId = event.gradedStudentPieceId,
                gradedPieceProblemIdAndResults = event.gradedPieceProblemIdAndResults,
            )
        )
    }

}