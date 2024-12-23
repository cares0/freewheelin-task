package freewheelin.pieceservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class AsyncConfig {

    @Bean
    fun asyncTaskExecutor(): ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            this.corePoolSize = 5
            this.maxPoolSize = 20
            this.queueCapacity = 20
            this.setThreadNamePrefix("async-thread")
            this.initialize()
        }
    }


}