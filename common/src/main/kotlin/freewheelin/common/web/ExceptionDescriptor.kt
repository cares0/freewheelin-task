package freewheelin.common.web

import org.springframework.http.HttpStatus

interface ExceptionDescriptor {

    val code: Int
    val message: String
    val httpStatus: HttpStatus

}