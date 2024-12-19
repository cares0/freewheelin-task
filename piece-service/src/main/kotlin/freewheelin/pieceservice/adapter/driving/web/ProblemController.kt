package freewheelin.pieceservice.adapter.driving.web

import freewheelin.common.mapper.MapperFactory
import freewheelin.pieceservice.adapter.driving.web.request.GetProblemByConditionRequest
import freewheelin.pieceservice.adapter.driving.web.response.GetProblemByConditionResponse
import freewheelin.pieceservice.application.dto.ProblemQueryCondition
import freewheelin.pieceservice.application.dto.ProblemQueryResult
import freewheelin.pieceservice.application.port.inbound.GetProblemByConditionUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/problems")
class ProblemController(
    private val mapperFactory: MapperFactory,
    private val getProblemByConditionUseCase: GetProblemByConditionUseCase,
) {

    @GetMapping
    fun getProblemByCondition(
        @ModelAttribute request: GetProblemByConditionRequest,
    ): List<GetProblemByConditionResponse> {
        val condition = mapperFactory
            .getMapper<GetProblemByConditionRequest, ProblemQueryCondition>()
            .map(request)

        val queryResult = getProblemByConditionUseCase.queryByCondition(condition)

        return mapperFactory
            .getMapper<ProblemQueryResult, GetProblemByConditionResponse>()
            .mapList(queryResult)
    }

}