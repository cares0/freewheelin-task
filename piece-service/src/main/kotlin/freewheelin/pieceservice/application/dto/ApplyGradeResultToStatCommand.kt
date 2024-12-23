package freewheelin.pieceservice.application.dto

data class ApplyGradeResultToStatCommand(
    val gradedStudentPieceId: Long,
    val gradedPieceProblemIdAndResults: List<PieceProblemIdAndResult>,
) {
}