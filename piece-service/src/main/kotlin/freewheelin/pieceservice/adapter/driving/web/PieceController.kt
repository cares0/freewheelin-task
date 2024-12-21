package freewheelin.pieceservice.adapter.driving.web

import freewheelin.common.mapper.MapperFactory
import freewheelin.common.web.ApiResponse
import freewheelin.pieceservice.adapter.driving.web.request.CreatePieceRequest
import freewheelin.pieceservice.application.dto.CreatePieceCommand
import freewheelin.pieceservice.application.dto.PublishPieceCommand
import freewheelin.pieceservice.application.port.inbound.CreatePieceUseCase
import freewheelin.pieceservice.application.port.inbound.PublishPieceUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/piece")
class PieceController(
    private val mapperFactory: MapperFactory,
    private val createPieceUseCase: CreatePieceUseCase,
    private val publishPieceUseCase: PublishPieceUseCase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPiece(
        @RequestBody request: CreatePieceRequest,
    ): ApiResponse<Long> {
        val savedId = createPieceUseCase.create(
            command = mapperFactory.getMapper<CreatePieceRequest, CreatePieceCommand>()
                .map(request)
        )
        return ApiResponse.ofCreated(savedId)
    }

    @PostMapping("/{pieceId}")
    fun publishPiece(
        @PathVariable pieceId: Long,
        @RequestParam studentIds: Set<String>,
    ): ApiResponse<Any> {
        publishPieceUseCase.publish(
            PublishPieceCommand(
                pieceId = pieceId,
                studentIds = studentIds,
            )
        )
        return ApiResponse.ofSuccess()
    }

}