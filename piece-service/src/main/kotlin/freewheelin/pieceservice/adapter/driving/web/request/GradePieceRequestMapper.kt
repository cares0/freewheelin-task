package freewheelin.pieceservice.adapter.driving.web.request

import freewheelin.common.mapper.Mapper
import freewheelin.pieceservice.application.dto.GradePieceBatchCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndAnswer
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.reflect.KClass

@Component
class GradePieceRequestMapper : Mapper<GradePieceRequest, GradePieceBatchCommand> {
    override val sourceType: KClass<GradePieceRequest> = GradePieceRequest::class
    override val targetType: KClass<GradePieceBatchCommand> = GradePieceBatchCommand::class

    override fun map(source: GradePieceRequest): GradePieceBatchCommand {
        return GradePieceBatchCommand(
            studentId = UUID.fromString(source.studentId),
            problemIdAndAnswers = source.problemIdAndAnswers.map {
                PieceProblemIdAndAnswer(
                    pieceProblemId = it.pieceProblemId,
                    answer = it.answer,
                )
            },
        )
    }
}