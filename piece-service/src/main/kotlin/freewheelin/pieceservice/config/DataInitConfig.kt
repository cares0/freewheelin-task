package freewheelin.pieceservice.config

import freewheelin.common.annotation.DefaultConstructor
import freewheelin.common.supports.logger
import freewheelin.pieceservice.domain.model.Problem
import freewheelin.pieceservice.domain.model.ProblemType
import freewheelin.pieceservice.domain.model.Unit
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

        val allUnits = entityManager
            .createQuery("select u from Unit u", Unit::class.java)
            .resultList
            .map(Unit::code)

        val allProblem = entityManager
            .createQuery("select p from Problem p", Problem::class.java)
            .resultList
            .map(Problem::id)

        val unitMap = mutableMapOf<String, Unit>()
        val newUnitData = unitData
            .drop(1)
            .filterNot { allUnits.contains(it.unitCode) }

        newUnitData.forEach { data ->
            val unit = Unit(
                code = data.unitCode,
                name = data.name,
                index = data.index,
            )
            entityManager.persist(unit)
            unitMap[unit.code] = unit
        }

        val newProblemData = problemData
            .drop(1)
            .filterNot { allProblem.contains(it.id) }
        
        newProblemData.forEach { data ->
            val unit = unitMap[data.unitCode]!!
            entityManager.persist(
                Problem(
                    id = data.id,
                    unit = unit,
                    level = data.level,
                    type = ProblemType.valueOf(data.type),
                    contents = "${unit.name}에 대한 문제입니다",
                    answer = data.answer,
                )
            )
        }

        log.info("데이터 초기화 완료: UNIT ${newUnitData.size}건 / PROBLEM: ${newProblemData.size}건")
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