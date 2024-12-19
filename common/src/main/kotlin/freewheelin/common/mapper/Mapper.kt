package freewheelin.common.mapper

import kotlin.reflect.KClass

interface Mapper<S : Any,T: Any> {

    val sourceType: KClass<S>
    val targetType: KClass<T>

    fun map(source: S): T
    fun mapList(sources: List<S>): List<T> = sources.map(::map)

}