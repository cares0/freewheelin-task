package freewheelin.pieceservice.adapter.driving.web

import freewheelin.pieceservice.adapter.driving.web.dsl.CreatePieceApiSpec
import freewheelin.pieceservice.adapter.driving.web.dsl.GetPieceWithProblemsApiSpec
import freewheelin.pieceservice.adapter.driving.web.dsl.GradePieceApiSpec
import freewheelin.pieceservice.adapter.driving.web.dsl.PublishPieceApiSpec
import freewheelin.pieceservice.adapter.driving.web.request.CreatePieceRequest
import freewheelin.pieceservice.adapter.driving.web.request.GradePieceRequest
import freewheelin.pieceservice.common.IntegrationTest
import freewheelin.pieceservice.domain.Piece
import freewheelin.pieceservice.domain.Problem
import freewheelin.pieceservice.domain.ProblemType
import io.github.cares0.restdocskdsl.dsl.*
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.generate.RestDocumentationGenerator
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.nio.charset.StandardCharsets
import java.util.UUID
import kotlin.random.Random

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

            mockMvc.post("/piece") {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/piece")
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

            mockMvc.post("/piece") {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/piece")
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

    @Nested
    @DisplayName("학습지 출제")
    inner class 학습지_출제 {

        @Test
        @DisplayName("정상적인 요청 시 생성된 학습지 ID를 응답한다.")
        fun normalTest() {
            val pieceId = stubbedPiece.id
            val studentIds = (1..10).map { UUID.randomUUID().toString() }

            mockMvc.post("/piece/{pieceId}", pieceId) {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/piece/{pieceId}")
                contentType = MediaType.APPLICATION_JSON
                characterEncoding = StandardCharsets.UTF_8.name()
                queryParam("studentIds", *studentIds.toTypedArray())
            }.andExpectAll {
                status { isOk() }
            }.andDo {
                print()
                document(PublishPieceApiSpec("publish-piece-normal")) {
                    pathVariables {
                        this.pieceId means "출제할 학습지 ID"
                    }
                    queryParameters {
                        this.studentIds means "출제 대상 학생 ID 리스트" typeOf ARRAY
                    }
                    responseBody {
                        this.responseTime means "응답 시간" typeOf DATETIME
                        this.code means "응답코드" typeOf STRING
                        this.data means "응답 데이터" typeOf NULL
                    }
                }
            }
        }

    }

    @Nested
    @DisplayName("학습지 문제 조회")
    inner class 학습지_문제_조회 {

        @Test
        @DisplayName("정상적인 요청 시 학습지와 문제 정보를 응답한다.")
        fun normalTest() {
            val pieceId = stubbedPiece.id

            mockMvc.get("/piece/problems") {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/piece/problems")
                contentType = MediaType.APPLICATION_JSON
                characterEncoding = StandardCharsets.UTF_8.name()
                queryParam("pieceId", pieceId.toString())
            }.andExpectAll {
                status { isOk() }
            }.andDo {
                print()
                document(GetPieceWithProblemsApiSpec("get-piece-with-problem-normal")) {
                    queryParameters {
                        this.pieceId means "조회할 학습지 ID" typeOf ARRAY
                    }
                    responseBody {
                        this.responseTime means "응답 시간" typeOf DATETIME
                        this.code means "응답코드" typeOf STRING
                        this.data means "응답 데이터" of {
                            this.pieceId means "학습지 ID" typeOf NUMBER
                            this.pieceName means "학습지 이름" typeOf STRING
                            this.problemCount means "학습지 문제 수" typeOf NUMBER
                            this.pieceProblems means "학습지 문제" of {
                                this.pieceProblemId means "학습지 문제 ID" typeOf NUMBER
                                this.problemId means "문제 ID" typeOf NUMBER
                                this.number means "문제 번호" typeOf NUMBER
                                this.level means "문제 난이도" typeOf NUMBER
                                this.type means "문제 유형" typeOf ENUM(ProblemType::class)
                                this.contents means "문제 내용" typeOf STRING
                            }
                        }
                    }
                }
            }
        }

    }


    @Nested
    @DisplayName("학습지 채점")
    inner class 학습지_채점 {

        @Test
        @DisplayName("정상적인 요청 시 학습지와 문제 정보를 응답한다.")
        fun normalTest() {
            val pieceId = stubbedPiece.id

            val normalRequest = GradePieceRequest(
                studentId = stubbedStudentId,
                problemIdAndAnswers = stubbedPiece.pieceProblems.map { pieceProblem ->
                    GradePieceRequest.PieceProblemIdAndAnswer(
                        pieceProblemId = pieceProblem.id,
                        answer = if (Random.Default.nextInt(1, 3) % 2 == 0) pieceProblem.problem.answer
                        else "fail"
                    )
                }
            )

            mockMvc.put("/piece/problems") {
                requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/piece/problems")
                contentType = MediaType.APPLICATION_JSON
                characterEncoding = StandardCharsets.UTF_8.name()
                queryParam("pieceId", pieceId.toString())
                content = createJson(normalRequest)
            }.andExpectAll {
                status { isOk() }
            }.andDo {
                print()
                handle(
                    /** REST Docs KDSL 오류로 인해 직접 작성 **/
                    MockMvcRestDocumentation.document(
                        "grade-piece-normal",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        queryParameters(
                            parameterWithName("pieceId")
                                .description("채점할 학습지 ID")
                        ),
                        requestFields(
                            fieldWithPath("studentId")
                                .type(JsonFieldType.STRING)
                                .description("학생 ID"),
                            subsectionWithPath("problemIdAndAnswers")
                                .type(JsonFieldType.ARRAY)
                                .description("문제 ID와 정답 리스트"),
                        ),
                        requestFields(
                            beneathPath("problemIdAndAnswers").withSubsectionId("problemIdAndAnswer"),
                            fieldWithPath("pieceProblemId")
                                .type(JsonFieldType.NUMBER)
                                .description("학습지 문제 ID"),
                            fieldWithPath("answer")
                                .type(JsonFieldType.STRING)
                                .description("정답"),
                        ),
                        responseFields(
                            beneathPath("data.[]").withSubsectionId("data"),
                            fieldWithPath("pieceProblemId")
                                .type(JsonFieldType.NUMBER)
                                .description("학습지 문제 ID"),
                            fieldWithPath("isSolved")
                                .type(JsonFieldType.BOOLEAN)
                                .description("정답인지 여부"),
                        )
                    )
                )

            }
        }

    }

    val stubbedUserId = UUID.randomUUID().toString()
    val stubbedStudentId = UUID.randomUUID().toString()
    lateinit var stubbedPiece: Piece

    @BeforeEach
    fun initTestData(
        @Autowired entityManager: EntityManager,
    ) {
        val problemsToAdd = entityManager
            .createQuery("select p from Problem p ", Problem::class.java)
            .setMaxResults(10)
            .resultList

        val piece = Piece.of("테스트 학습지", stubbedUserId)
        piece.addProblems(problemsToAdd)
        piece.publishBatch(setOf(stubbedStudentId))

        stubbedPiece = piece

        entityManager.persist(piece)
    }

}