package freewheelin.pieceservice

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

//@SpringBootTest
class PieceServiceApplicationTests {

	@Test
	fun contextLoads() {
		repeat(5) {
			println(UUID.randomUUID().toString())
		}
	}

}
