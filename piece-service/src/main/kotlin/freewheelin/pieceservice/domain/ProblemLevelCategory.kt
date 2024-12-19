package freewheelin.pieceservice.domain

enum class ProblemLevelCategory(
    val levelRange: IntRange,
) {

    LOW(levelRange = 1..1),
    MIDDLE(levelRange = 2..4),
    HIGH(levelRange = 5..5),

    ;

    companion object {
        private val levelToCategoryMap = entries.flatMap { levelRange ->
            levelRange.levelRange.map { it to levelRange }
        }.toMap()

        fun fromLevel(level: Int): ProblemLevelCategory {
            return levelToCategoryMap[level]
                ?: throw IllegalArgumentException("레벨에 해당하는 범위가 없습니다.")
        }
    }

}