package freewheelin.common.supports

inline fun <reified T : Enum<T>> Enum<*>.toOtherEnumOrNull(): T? {
    return enumValues<T>().find { it.name == this.name }
}