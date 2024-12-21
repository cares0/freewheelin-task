package freewheelin.pieceservice.supports

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import freewheelin.common.web.ApiResponse
import freewheelin.common.web.ErrorData
import freewheelin.common.web.ExceptionDescriptor
import freewheelin.common.web.ValidationFailException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice(
    val log: Logger = LoggerFactory.getLogger(ControllerAdvice::class.java)
) {

    @ExceptionHandler
    fun commonExceptionHandler(e: Exception): ResponseEntity<ApiResponse<ErrorData<String>>> {

        val exceptionDescriptor = ExceptionContainer.of(e::class)

        return if (exceptionDescriptor != null) {
            log.error("[ERROR]", e)
            createErrorResponse(exceptionDescriptor)
        } else {
            log.error("[ERROR]", e)
            logAboutExceptionRegistration(e)
            createUnRegisteredErrorResponse(e.message)
        }

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun httpMessageNotReadableExceptionHandler(e: HttpMessageNotReadableException): ApiResponse<ErrorData<String>> {

        log.error("[ERROR]", e)

        val message = when (val exceptionCause = e.cause) {
            is InvalidFormatException -> {
                "입력 받은 ${exceptionCause.value}를 ${exceptionCause.targetType.simpleName}타입으로 변환 중 에러가 발생했습니다."
            }

            else -> "요청을 역직렬화 하는과정에서 예외가 발생했습니다."
        }

        return ApiResponse.ofCustomError(
            ExceptionContainer.HTTP_MESSAGE_NOT_READABLE_EXCEPTION,
            message
        )
    }

    private fun createUnRegisteredErrorResponse(
        message: String?
    ): ResponseEntity<ApiResponse<ErrorData<String>>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.ofUnRegisteredError(message ?: "No Message"))
    }

    private fun createErrorResponse(
        exceptionDescriptor: ExceptionDescriptor
    ): ResponseEntity<ApiResponse<ErrorData<String>>> {
        return ResponseEntity.status(exceptionDescriptor.httpStatus)
            .body(ApiResponse.ofError(exceptionDescriptor))
    }

    private fun logAboutExceptionRegistration(e: Throwable) {
        log.warn(
            "\nExceptionDescriptor에 [${e::class.simpleName}] 예외를 등록하세요 \n" +
                    "발생한 예외에 대해 Internal Server Error 상태 코드를 응답하고 있습니다. \n" +
                    "예외 상황에 대해 상세한 서버 예외 메시지가 외부로 노출될 위험이 있습니다."
        )
    }


}