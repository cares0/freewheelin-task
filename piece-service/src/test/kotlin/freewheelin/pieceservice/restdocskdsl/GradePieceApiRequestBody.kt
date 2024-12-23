package freewheelin.pieceservice.restdocskdsl

import io.github.cares0.restdocskdsl.dsl.ApiComponent
import io.github.cares0.restdocskdsl.dsl.ApiSpec
import io.github.cares0.restdocskdsl.dsl.BodyComponent
import io.github.cares0.restdocskdsl.dsl.JsonField
import io.github.cares0.restdocskdsl.dsl.NestedArrayJsonField
import io.github.cares0.restdocskdsl.dsl.NestedJsonField
import io.github.cares0.restdocskdsl.dsl.QueryParameterField
import io.github.cares0.restdocskdsl.dsl.QueryParameterSnippetGenerator
import io.github.cares0.restdocskdsl.dsl.RequestBodySnippetGenerator
import io.github.cares0.restdocskdsl.dsl.ResponseBodySnippetGenerator
import kotlin.String
import kotlin.collections.List
import kotlin.collections.MutableList
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.snippet.Snippet

/** https://github.com/cares0/rest-docs-kdsl/issues/1 해결 전 대안 **/
public object GradePieceApiRequestBody : BodyComponent(false) {
  public val studentId: JsonField = JsonField("studentId", false, 0)

  public val problemIdAndAnswers: NestedArrayJsonField<PieceProblemIdAndAnswer_0> =
      NestedArrayJsonField("problemIdAndAnswers", PieceProblemIdAndAnswer_0, false, 0)

  init {
    addFields(
      `studentId`,
      `problemIdAndAnswers`
    )
  }

  public object PieceProblemIdAndAnswer_0 : BodyComponent(false) {
    public val pieceProblemId: JsonField = JsonField("pieceProblemId", true, 1)

    public val answer: JsonField = JsonField("answer", true, 1)

    init {
      addFields(
        `pieceProblemId`,
        `answer`
      )
    }
  }
}

public object GradePieceApiResponseBody : BodyComponent(false) {
  public val code: JsonField = JsonField("code", false, 0)

  public val `data`: NestedJsonField<List_0> = NestedJsonField("data", List_0, false, 0)

  public val responseTime: JsonField = JsonField("responseTime", false, 0)

  init {
    addFields(
      `code`,
      `data`,
      `responseTime`
    )
  }

  public object List_0 : BodyComponent(false) {
    public val pieceProblemId: JsonField = JsonField("pieceProblemId", true, 0)
    public val result: JsonField = JsonField("result", true, 0)

    init {
      addFields(
        `pieceProblemId`,
        `result`,
      )
    }
  }
}

public object GradePieceApiQueryParameter : ApiComponent<ParameterDescriptor>() {
  public val pieceId: QueryParameterField = QueryParameterField("pieceId")

  init {
    addFields(
      `pieceId`
    )
  }
}

public data class GradePieceApiSpec(
  override val identifier: String,
) : ApiSpec,
    RequestBodySnippetGenerator<GradePieceApiRequestBody>,
    ResponseBodySnippetGenerator<GradePieceApiResponseBody>,
    QueryParameterSnippetGenerator<GradePieceApiQueryParameter> {
  override val snippets: MutableList<Snippet> = mutableListOf()

  override fun getRequestBodyApiComponent(): GradePieceApiRequestBody = GradePieceApiRequestBody

  override fun getResponseBodyApiComponent(): GradePieceApiResponseBody = GradePieceApiResponseBody

  override fun getQueryParameterApiComponent(): GradePieceApiQueryParameter =
      GradePieceApiQueryParameter

  override fun addSnippet(generatedSnippet: Snippet) {
    this.snippets.add(generatedSnippet)
  }

  override fun addSnippets(generatedSnippets: List<Snippet>) {
    this.snippets.addAll(generatedSnippets)
  }
}
