package freewheelin.pieceservice.supports

import freewheelin.common.web.ExceptionDescriptor
import freewheelin.common.supports.upperCamelToUpperSnake
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import kotlin.reflect.KClass

enum class ExceptionContainer(
    override val code: Int,
    override val httpStatus: HttpStatus,
    override val message: String,
) : ExceptionDescriptor {

    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(
        code = 1000,
        httpStatus = BAD_REQUEST,
        message = "JSON 타입의 요청 데이터를 역직렬화 시 발생할 수 있는 예외입니다.",
    ),

    PIECE_PROBLEM_COUNT_EXCEED_EXCEPTION(
        code = 1001,
        httpStatus = BAD_REQUEST,
        message = "학습지 생성 시 문제 수가 초과된 경우입니다.",
    ),

    ENTITY_NOT_EXIST_EXCEPTION(
        code = 1002,
        httpStatus = BAD_REQUEST,
        message = "요청 경로변수, 파라미터, 바디 내에 포함된 식별자 값으로 리소스를 조회할 수 없을 경우 발생합니다.",
    ),

    NO_RESOURCE_FOUND_EXCEPTION(
        code = 1003,
        httpStatus = BAD_REQUEST,
        message = "정적 리소스를 찾을 수 없을 경우 발생합니다.",
    ),

    CANNOT_GRADE_PIECE_EXCEPTION(
        code = 1004,
        httpStatus = BAD_REQUEST,
        message = "채점할 수 없는 문제입니다.",
    ),

    ;

    companion object {
        fun of(exceptionClass: KClass<*>): ExceptionDescriptor? {
            return entries.firstOrNull { enum -> enum.name == exceptionClass.simpleName!!.upperCamelToUpperSnake() }
        }
    }

}