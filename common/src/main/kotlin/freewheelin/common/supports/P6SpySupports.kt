package freewheelin.common.supports

import com.p6spy.engine.common.P6Util
import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle

class PrettyP6spyFormatter : MessageFormattingStrategy {

    override fun formatMessage(
        connectionId: Int,
        now: String?,
        elapsed: Long,
        category: String?,
        prepared: String?,
        sql: String?,
        url: String?
    ): String {
        val formattedSql = formatSql(category, sql)
        return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + formattedSql
    }

    private fun formatSql(category: String?, sql: String?): String? {

        if (sql == null || sql.trim { it <= ' ' } == "") return sql

        val prettyFormatSql: String
        if (Category.STATEMENT.name.equals(category)) {
            val tempSql = sql.trim { it <= ' ' }.lowercase()
            prettyFormatSql =
                if (tempSql.startsWith("create")
                    || tempSql.startsWith("alter")
                    || tempSql.startsWith("comment")
                ) {
                    FormatStyle.DDL.formatter.format(sql)
                } else {
                    FormatStyle.BASIC.formatter.format(sql)
                }
        } else {
            return sql
        }
        return "$prettyFormatSql\n"
    }
}