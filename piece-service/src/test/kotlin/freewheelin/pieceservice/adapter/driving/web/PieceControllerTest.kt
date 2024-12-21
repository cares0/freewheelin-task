package freewheelin.pieceservice.adapter.driving.web

import freewheelin.pieceservice.adapter.driving.web.dsl.CreatePieceApiSpec
import freewheelin.pieceservice.adapter.driving.web.request.CreatePieceRequest
import freewheelin.pieceservice.common.IntegrationTest
import io.github.cares0.restdocskdsl.dsl.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.generate.RestDocumentationGenerator
import org.springframework.test.web.servlet.post
import java.nio.charset.StandardCharsets
import java.util.UUID

class PieceControllerTest : IntegrationTest() {

    @Nested
    @DisplayName("학습지 생성")
    inner class 학습지_생성 {

        @Test
        @DisplayName("정상적인 요청 시 생성된 학습지 ID를 응답한다.")
        fun normalTest() {
            val normalRequest = CreatePieceRequest(
                makerId = UUID.randomUUID().toString(),
                name = "테스트 학습지",
                problemIdsToAdd = setOf(1000, 1001, 1002, 1003),
            )

            mockMvc.post("/pieces") {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/pieces")
                contentType = MediaType.APPLICATION_JSON
                characterEncoding = StandardCharsets.UTF_8.name()
                content = createJson(normalRequest)
            }.andExpectAll {
                status { isCreated() }
            }.andDo {
                print()
                document(CreatePieceApiSpec("create-piece-normal")) {
                    requestBody {
                        this.makerId means "만든이 ID" typeOf STRING
                        this.name means "학습지 이름" typeOf STRING
                        this.problemIdsToAdd means "추가할 문제 ID 리스트" typeOf ARRAY
                    }
                    responseBody {
                        this.responseTime means "응답 시간" typeOf DATETIME
                        this.code means "응답코드" typeOf STRING
                        this.data means "만들어진 학습지 ID" typeOf NUMBER
                    }
                }
            }
        }

        @Test
        @DisplayName("문제 수가 초과된 경우 예외를 발생시킨다.")
        fun whenProblemCountExceed() {
            val countExceedRequest = CreatePieceRequest(
                makerId = UUID.randomUUID().toString(),
                name = "테스트 학습지",
                problemIdsToAdd = (1..100).map { it + 1000L }.toSet(),
            )

            mockMvc.post("/pieces") {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/pieces")
                contentType = MediaType.APPLICATION_JSON
                characterEncoding = StandardCharsets.UTF_8.name()
                content = createJson(countExceedRequest)
            }.andExpectAll {
                status { isBadRequest() }
            }.andDo {
                print()
                document(CreatePieceApiSpec("create-piece-error-problem-count-exceed")) {
                    requestBody {
                        this.makerId means "만든이 ID" typeOf STRING
                        this.name means "학습지 이름" typeOf STRING
                        this.problemIdsToAdd means "추가할 문제 ID 리스트" typeOf ARRAY
                    }
                }
            }
        }
    }

}