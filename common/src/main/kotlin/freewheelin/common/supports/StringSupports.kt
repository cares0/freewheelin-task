package freewheelin.common.supports

import com.google.common.base.CaseFormat.*

fun String.upperCamelToUpperSnake(): String =
    UPPER_CAMEL.to(UPPER_UNDERSCORE, this)