package freewheelin.pieceservice.config

import com.p6spy.engine.spy.P6SpyOptions
import freewheelin.common.supports.PrettyP6spyFormatter
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class P6SpyConfig {

    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat =
            PrettyP6spyFormatter::class.java.name
    }

}