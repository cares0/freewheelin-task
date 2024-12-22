package freewheelin.pieceservice.adapter.driving.web.request

import freewheelin.common.mapper.Mapper
import freewheelin.pieceservice.application.dto.GradePieceForStudentBatchCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndAnswer
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.reflect.KClass

@Component
class GradePieceRequestMapper : Mapper<GradePieceRequest, GradePieceForStudentBatchCommand> {
    override val sourceType: KClass<GradePieceRequest> = GradePieceRequest::class
    override val targetType: KClass<GradePieceForStudentBatchCommand> = GradePieceForStudentBatchCommand::class

    override fun map(source: GradePieceRequest): GradePieceForStudentBatchCommand {
        return GradePieceForStudentBatchCommand(
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