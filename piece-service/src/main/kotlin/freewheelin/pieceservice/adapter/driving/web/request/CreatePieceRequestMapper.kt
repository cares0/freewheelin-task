package freewheelin.pieceservice.adapter.driving.web.request

import freewheelin.common.mapper.Mapper
import freewheelin.pieceservice.application.dto.CreatePieceCommand
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class CreatePieceRequestMapper : Mapper<CreatePieceRequest, CreatePieceCommand> {
    override val sourceType: KClass<CreatePieceRequest> = CreatePieceRequest::class
    override val targetType: KClass<CreatePieceCommand> = CreatePieceCommand::class

    override fun map(source: CreatePieceRequest): CreatePieceCommand {
        return CreatePieceCommand(
            makerId = source.makerId,
            name = source.name,
            problemIdsToAdd = source.problemIdsToAdd,
        )
    }
}