package freewheelin.common.web

data class ErrorData<T>(
    val errorCode: Int?,
    val message: T
) {

    companion object {

        fun of(exceptionDescriptor: ExceptionDescriptor): ErrorData<String> {
            return ErrorData(
                errorCode = exceptionDescriptor.code,
                message = exceptionDescriptor.message
            )
        }

        fun <T> ofCustomMessage(
            exceptionDescriptor: ExceptionDescriptor,
            customMessage: T
        ): ErrorData<T> {
            return ErrorData(
                errorCode = exceptionDescriptor.code,
                message = customMessage
            )
        }

        fun ofUnRegistered(message: String): ErrorData<String> {
            return ErrorData(
                errorCode = null,
                message = message
            )
        }

    }
}