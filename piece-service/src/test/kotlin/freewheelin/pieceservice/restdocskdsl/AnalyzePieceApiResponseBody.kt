package freewheelin.pieceservice.restdocskdsl

import io.github.cares0.restdocskdsl.dsl.ApiComponent
import io.github.cares0.restdocskdsl.dsl.ApiSpec
import io.github.cares0.restdocskdsl.dsl.BodyComponent
import io.github.cares0.restdocskdsl.dsl.JsonField
import io.github.cares0.restdocskdsl.dsl.NestedArrayJsonField
import io.github.cares0.restdocskdsl.dsl.NestedJsonField
import io.github.cares0.restdocskdsl.dsl.QueryParameterField
import io.github.cares0.restdocskdsl.dsl.QueryParameterSnippetGenerator
import io.github.cares0.restdocskdsl.dsl.ResponseBodySnippetGenerator
import kotlin.String
import kotlin.collections.List
import kotlin.collections.MutableList
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.snippet.Snippet

public object AnalyzePieceApiResponseBody : BodyComponent(false) {
  public val code: JsonField = JsonField("code", false, 0)

  public val `data`: NestedJsonField<AnalyzePieceResponse_0> = NestedJsonField("data",
    AnalyzePieceResponse_0, false, 0)

  public val responseTime: JsonField = JsonField("responseTime", false, 0)

  init {
    addFields(
      `code`,
      `data`,
      `responseTime`
    )
  }

  public object AnalyzePieceResponse_0 : BodyComponent(false) {
    public val pieceId: JsonField = JsonField("pieceId", false, 0)

    public val pieceName: JsonField = JsonField("pieceName", false, 0)

    public val publishedStudentIds: JsonField =
      JsonField("publishedStudentIds",true, 0)

    public val studentStats: NestedArrayJsonField<StudentStatResponse_0> =
      NestedArrayJsonField("studentStats", StudentStatResponse_0, false, 0)

    public val problemStats: NestedArrayJsonField<ProblemStatResponse_0> =
      NestedArrayJsonField("problemStats", ProblemStatResponse_0, false, 0)

    init {
      addFields(
        `pieceId`,
        `pieceName`,
        `publishedStudentIds`,
        `studentStats`,
        `problemStats`
      )
    }

    public object StudentStatResponse_0 : BodyComponent(false) {
      public val studentId: JsonField = JsonField("studentId", true, 1)

      public val solvedPercentage: JsonField = JsonField("solvedPercentage", true, 1)

      init {
        addFields(
          `studentId`,
          `solvedPercentage`
        )
      }
    }

    public object ProblemStatResponse_0 : BodyComponent(false) {
      public val problemId: JsonField = JsonField("problemId", true, 1)

      public val number: JsonField = JsonField("number", true, 1)

      public val solvedStudentPercentage: JsonField = JsonField("solvedStudentPercentage", true, 1)

      init {
        addFields(
          `problemId`,
          `number`,
          `solvedStudentPercentage`
        )
      }
    }
  }
}

public object AnalyzePieceApiQueryParameter : ApiComponent<ParameterDescriptor>() {
  public val pieceId: QueryParameterField = QueryParameterField("pieceId")

  init {
    addFields(
      `pieceId`
    )
  }
}

public data class AnalyzePieceApiSpec(
  override val identifier: String,
) : ApiSpec,
  ResponseBodySnippetGenerator<AnalyzePieceApiResponseBody>,
  QueryParameterSnippetGenerator<AnalyzePieceApiQueryParameter> {
  override val snippets: MutableList<Snippet> = mutableListOf()

  override fun getResponseBodyApiComponent(): AnalyzePieceApiResponseBody =
    AnalyzePieceApiResponseBody

  override fun getQueryParameterApiComponent(): AnalyzePieceApiQueryParameter =
    AnalyzePieceApiQueryParameter

  override fun addSnippet(generatedSnippet: Snippet) {
    this.snippets.add(generatedSnippet)
  }

  override fun addSnippets(generatedSnippets: List<Snippet>) {
    this.snippets.addAll(generatedSnippets)
  }
}