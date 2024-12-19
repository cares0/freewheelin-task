package freewheelin.pieceservice.common

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class IntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun <T> createJson(request: T): String {
        return objectMapper.writeValueAsString(request)
    }

}