package freewheelin.pieceservice.adapter.driving.web.request

import freewheelin.common.mapper.Mapper
import freewheelin.pieceservice.application.dto.GradePieceForStudentCommand
import freewheelin.pieceservice.application.dto.PieceProblemIdAndAnswer
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.reflect.KClass

@Component
class GradePieceRequestMapper : Mapper<GradePieceRequest, GradePieceForStudentCommand> {
    override val sourceType: KClass<GradePieceRequest> = GradePieceRequest::class
    override val targetType: KClass<GradePieceForStudentCommand> = GradePieceForStudentCommand::class

    override fun map(source: GradePieceRequest): GradePieceForStudentCommand {
        return GradePieceForStudentCommand(
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