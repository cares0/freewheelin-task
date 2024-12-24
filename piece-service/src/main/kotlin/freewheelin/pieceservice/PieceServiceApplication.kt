package freewheelin.pieceservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableRetry
class PieceServiceApplication

fun main(args: Array<String>) {
	runApplication<PieceServiceApplication>(*args)
}
