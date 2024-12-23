package freewheelin.pieceservice.domain.event

import freewheelin.common.event.Event
import freewheelin.pieceservice.application.dto.PieceProblemIdAndResult

data class PieceGradedEvent(
    val gradedStudentPieceId: Long,
    val gradedPieceProblemIdAndResults: List<PieceProblemIdAndResult>,
) : Event()