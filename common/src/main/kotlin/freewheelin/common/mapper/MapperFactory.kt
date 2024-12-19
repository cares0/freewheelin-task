package freewheelin.common.mapper

import kotlin.reflect.KClass

class MapperFactory(
    mappers: List<Mapper<*, *>>
) {

    val mapperMap: Map<Pair<KClass<*>, KClass<*>>, Mapper<*, *>> =
        mappers.associateBy { it.sourceType to it.targetType }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified S : Any, reified T: Any> getMapper(): Mapper<S, T> {
        return mapperMap[S::class to T::class] as? Mapper<S, T>
            ?: throw IllegalStateException("적합한 매퍼 구현체가 없습니다.")
    }

}