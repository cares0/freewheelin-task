package freewheelin.pieceservice.config

import freewheelin.common.annotation.DefaultConstructor
import freewheelin.common.supports.logger
import freewheelin.pieceservice.domain.Problem
import freewheelin.pieceservice.domain.ProblemType
import freewheelin.pieceservice.domain.Unit
import io.github.millij.poi.ss.model.annotations.Sheet
import io.github.millij.poi.ss.model.annotations.SheetColumn
import io.github.millij.poi.ss.reader.XlsxReader
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource

@Configuration
class DataConfig(
    private val entityManager: EntityManager,
) {

    val log = logger()

    @EventListener
    @Transactional
    fun initializeProblemData(event: ApplicationReadyEvent) {
        val resource = ClassPathResource("data/problem.xlsx")
        val reader = XlsxReader()
        val unitData = reader.read(UnitCodeExcelData::class.java, resource.inputStream, 1)
        val problemData = reader.read(ProblemExcelData::class.java, resource.inputStream, 0)

        val unitMap = mutableMapOf<String, Unit>()
        unitData.drop(1).forEach { data ->
            val unit = Unit(
                code = data.unitCode,
                name = data.name,
                index = data.index,
            )
            entityManager.persist(unit)
            unitMap[unit.code] = unit
        }

        problemData.drop(1).forEach { data ->
            entityManager.persist(
                Problem(
                    id = data.id,
                    unit = unitMap[data.unitCode]!!,
                    level = data.level,
                    type = ProblemType.valueOf(data.type),
                    answer = data.answer,
                )
            )
        }

        log.info("데이터 초기화 완료: UNIT ${unitData.size}건/PROBLEM: ${problemData.size}건")
    }

}

@Sheet
@DefaultConstructor
data class ProblemExcelData(
    @SheetColumn("문제id") var id: Long,
    @SheetColumn("유형코드") var unitCode: String,
    @SheetColumn("난이도") var level: Int,
    @SheetColumn("문제유형") var type: String,
    @SheetColumn("정답") var answer: String,
)

@Sheet
@DefaultConstructor
data class UnitCodeExcelData(
    @SheetColumn("인덱스") var index: Int,
    @SheetColumn("유형코드") var unitCode: String,
    @SheetColumn("유형이름") var name: String,
)