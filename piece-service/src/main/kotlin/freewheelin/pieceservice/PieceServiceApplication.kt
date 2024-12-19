package freewheelin.pieceservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PieceServiceApplication

fun main(args: Array<String>) {
	runApplication<PieceServiceApplication>(*args)
}
