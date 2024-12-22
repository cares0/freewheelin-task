package freewheelin.pieceservice.adapter.driving.web

import freewheelin.common.mapper.MapperFactory
import freewheelin.common.web.ApiResponse
import freewheelin.pieceservice.adapter.driving.web.request.CreatePieceRequest
import freewheelin.pieceservice.adapter.driving.web.request.GradePieceRequest
import freewheelin.pieceservice.adapter.driving.web.response.GetPieceWithProblemResponse
import freewheelin.pieceservice.adapter.driving.web.response.GradePieceResponse
import freewheelin.pieceservice.application.dto.*
import freewheelin.pieceservice.application.port.inbound.CreatePieceUseCase
import freewheelin.pieceservice.application.port.inbound.GetPieceWithProblemUseCase
import freewheelin.pieceservice.application.port.inbound.GradePieceForStudentUseCase
import freewheelin.pieceservice.application.port.inbound.PublishPieceUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/piece")
class PieceController(
    private val mapperFactory: MapperFactory,
    private val createPieceUseCase: CreatePieceUseCase,
    private val publishPieceUseCase: PublishPieceUseCase,
    private val getPieceWithProblemUseCase: GetPieceWithProblemUseCase,
    private val gradePieceForStudentUseCase: GradePieceForStudentUseCase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPiece(
        @RequestBody request: CreatePieceRequest,
    ): ApiResponse<Long> {
        val savedId = createPieceUseCase.create(
            command = mapperFactory
                .getMapper<CreatePieceRequest, CreatePieceCommand>()
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
            pieceId = pieceId,
            command = PublishPieceCommand(studentIds)
        )
        return ApiResponse.ofSuccess()
    }

    @GetMapping("/problems")
    fun getPieceWithProblems(
        @RequestParam pieceId: Long,
    ): ApiResponse<GetPieceWithProblemResponse> {
        val queryResult = getPieceWithProblemUseCase.queryWithProblemById(pieceId)
        return ApiResponse.ofSuccess(
            mapperFactory
                .getMapper<PieceWithProblemQueryResult, GetPieceWithProblemResponse>()
                .map(queryResult)
        )
    }

    @PutMapping("/problems")
    fun gradePiece(
        @RequestParam pieceId: Long,
        @RequestBody request: GradePieceRequest,
    ): ApiResponse<List<GradePieceResponse>> {
        val gradeResults = gradePieceForStudentUseCase.gradeBatch(
            pieceId = pieceId,
            command = mapperFactory.getMapper<GradePieceRequest, GradePieceForStudentBatchCommand>()
                .map(request)
        )

        return ApiResponse.ofSuccess(
            mapperFactory.getMapper<PieceProblemIdAndResult, GradePieceResponse>()
                .mapList(gradeResults)
        )
    }

}