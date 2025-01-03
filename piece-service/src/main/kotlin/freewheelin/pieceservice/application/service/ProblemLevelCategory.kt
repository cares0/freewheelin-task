package freewheelin.pieceservice.application.service

enum class ProblemLevelCategory(
    val levelRange: IntRange,
    private val lowLevelRatio: Double,
    private val middleLevelRatio: Double,
    private val highLevelRatio: Double,
) {

    LOW(
        levelRange = 1..1,
        lowLevelRatio = 0.5,
        middleLevelRatio = 0.3,
        highLevelRatio = 0.2,
    ),
    MIDDLE(
        levelRange = 2..4,
        lowLevelRatio = 0.25,
        middleLevelRatio = 0.5,
        highLevelRatio = 0.25,
    ),
    HIGH(
        levelRange = 5..5,
        lowLevelRatio = 0.2,
        middleLevelRatio = 0.3,
        highLevelRatio = 0.5,
    ),

    ;

    fun getRatioMap() = ratioPerCategory[this]!!

    companion object {

        private val categoryPerLevel = entries.flatMap { levelRange ->
            levelRange.levelRange.map { it to levelRange }
        }.toMap()

        private val ratioPerCategory = entries.associateWith { category ->
            makeRatioMap(
                lowLevelRatio = category.lowLevelRatio,
                middleLevelRatio = category.middleLevelRatio,
                highLevelRatio = category.highLevelRatio,
            )
        }

        private fun makeRatioMap(
            lowLevelRatio: Double,
            middleLevelRatio: Double,
            highLevelRatio: Double
        ) = mapOf(
            LOW to lowLevelRatio,
            MIDDLE to middleLevelRatio,
            HIGH to highLevelRatio,
        )

    }

}