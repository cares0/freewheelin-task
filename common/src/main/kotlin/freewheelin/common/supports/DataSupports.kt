package freewheelin.common.supports

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.repository.CrudRepository
import kotlin.reflect.KClass

fun nullSafeBuilder(booleanFunction: () -> BooleanExpression): BooleanBuilder {
    return try {
        BooleanBuilder(booleanFunction.invoke())
    } catch (e: IllegalArgumentException) {
        BooleanBuilder()
    }
}

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID & Any): T =
    findById(id).orElseThrow {throw EntityNotExistException(T::class, id.toString()) }

class EntityNotExistException(
    entityClassToFind: KClass<*>,
    identifier: String
) : java.lang.IllegalArgumentException(
    "[$identifier] 식별자에 해당하는 [${entityClassToFind.simpleName}] 엔티티를 찾을 수 없습니다."
)