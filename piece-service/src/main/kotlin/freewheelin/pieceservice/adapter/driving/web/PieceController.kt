package freewheelin.pieceservice.adapter.driving.web

import freewheelin.common.mapper.MapperFactory
import freewheelin.common.web.ApiResponse
import freewheelin.pieceservice.adapter.driving.web.request.CreatePieceRequest
import freewheelin.pieceservice.adapter.driving.web.request.GradePieceRequest
import freewheelin.pieceservice.adapter.driving.web.response.AnalyzePieceResponse
import freewheelin.pieceservice.adapter.driving.web.response.GetPieceWithProblemResponse
import freewheelin.pieceservice.adapter.driving.web.response.GradePieceResponse
import freewheelin.pieceservice.application.dto.*
import freewheelin.pieceservice.application.port.inbound.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/piece")
class PieceController(
    private val mapperFactory: MapperFactory,
    private val createPieceUseCase: CreatePieceUseCase,
    private val publishPieceUseCase: PublishPieceUseCase,
    private val getPieceWithProblemUseCase: GetPieceWithProblemUseCase,
    private val analyzePieceUseCase: AnalyzePieceUseCase,
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
            command = PublishPieceCommand(studentIds.map(UUID::fromString).toSet())
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
        val gradeResults = gradePieceForStudentUseCase.grade(
            pieceId = pieceId,
            command = mapperFactory.getMapper<GradePieceRequest, GradePieceForStudentCommand>()
                .map(request)
        )

        return ApiResponse.ofSuccess(
            mapperFactory.getMapper<PieceProblemIdAndResult, GradePieceResponse>()
                .mapList(gradeResults)
        )
    }

    @GetMapping("/analyze")
    fun analyzePiece(
        @RequestParam pieceId: Long,
    ): ApiResponse<AnalyzePieceResponse> {
        val result = analyzePieceUseCase.analyze(pieceId)

        return ApiResponse.ofSuccess(
            mapperFactory.getMapper<AnalyzePieceResult, AnalyzePieceResponse>()
                .map(result)
        )
    }

}