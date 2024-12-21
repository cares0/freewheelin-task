package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.CreatePieceCommand
import freewheelin.pieceservice.application.port.inbound.CreatePieceUseCase
import freewheelin.pieceservice.application.port.outbound.PieceSavePort
import freewheelin.pieceservice.application.port.outbound.ProblemLoadPort
import freewheelin.pieceservice.domain.Piece
import org.springframework.stereotype.Service

@Service
class PieceService(
    private val problemLoadPort: ProblemLoadPort,
    private val pieceSavePort: PieceSavePort
) : CreatePieceUseCase {

    override fun create(command: CreatePieceCommand): Long {
        val newPiece = Piece.of(
            name = command.name,
            makerId = command.makerId
        )

        val problemsToAdd = problemLoadPort.loadBatchById(command.problemIdsToAdd)

        newPiece.addProblems(problemsToAdd)

        val savedId = pieceSavePort.save(newPiece)

        return savedId
    }

}