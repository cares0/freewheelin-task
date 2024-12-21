package freewheelin.common.response

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiResponse<T>(

    val responseTime: LocalDateTime,
    val code: HttpStatus,
    val data: T?

) {

    companion object {
        fun <T> ofCustom(code: HttpStatus, data: T? = null): ApiResponse<T> {
            return ApiResponse(
                responseTime = LocalDateTime.now(),
                code = code,
                data = data
            )
        }


        fun <T> ofSuccess(data: T? = null): ApiResponse<T> {
            return ApiResponse(
                responseTime = LocalDateTime.now(),
                code = HttpStatus.OK,
                data = data
            )
        }

        fun <T> ofCreated(data: T? = null): ApiResponse<T> {
            return ApiResponse(
                responseTime = LocalDateTime.now(),
                code = HttpStatus.CREATED,
                data = data
            )
        }
    }
}