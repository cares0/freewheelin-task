package freewheelin.pieceservice.adapter.driving.web

import freewheelin.pieceservice.adapter.driving.web.dsl.GetProblemByConditionApiSpec
import freewheelin.pieceservice.adapter.driving.web.request.GetProblemByConditionRequest
import freewheelin.pieceservice.adapter.driving.web.response.GetProblemByConditionResponse
import freewheelin.pieceservice.common.IntegrationTest
import io.github.cares0.restdocskdsl.dsl.*
import org.hamcrest.Matchers
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.generate.RestDocumentationGenerator
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.nio.charset.StandardCharsets

class ProblemControllerTest : IntegrationTest() {

    @Nested
    @DisplayName("문제 조회")
    inner class 문제_조회 {

        @Test
        @DisplayName("정상적인 요청 시 조건에 맞는 문제 리스트를 응답한다.")
        fun normalTest() {
            mockMvc.get("/problems") {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/problems")
                contentType = MediaType.APPLICATION_JSON
                characterEncoding = StandardCharsets.UTF_8.name()
                param("totalCount", "10")
                param("unitCodeList", "uc1580", "uc1581")
                param("level", "LOW")
                param("problemType", "ALL")
            }.andExpectAll {
                status { isOk() }
                // match { jsonPath("$.*.id", Matchers.equalTo(1)) }
            }.andDo {
                print()
                document(GetProblemByConditionApiSpec("get-problem-normal")) {
                    queryParameters {
                        this.totalCount means "총 문제수" typeOf NUMBER
                        this.unitCodeList means "유형 코드 리스트" typeOf ARRAY
                        this.problemType means "문제 유형" typeOf ENUM(GetProblemByConditionRequest.ProblemType::class)
                        this.level means "총 문제수" typeOf ENUM(GetProblemByConditionRequest.Level::class)
                    }
                    responseBody {
                        this.id means "문제 ID" typeOf NUMBER
                        this.answer means "정답" typeOf STRING
                        this.unitCode means "유형코드" typeOf STRING
                        this.level means "난이도" typeOf NUMBER
                        this.problemType means "문제유형" typeOf ENUM(GetProblemByConditionResponse.ProblemType::class)
                    }
                }
            }
        }

    }

}