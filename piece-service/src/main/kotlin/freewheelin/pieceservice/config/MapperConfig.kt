package freewheelin.pieceservice.config

import freewheelin.common.mapper.Mapper
import freewheelin.common.mapper.MapperFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MapperConfig {

    @Bean
    fun mapperFactory(mappers: List<Mapper<*, *>>) = MapperFactory(mappers)

}