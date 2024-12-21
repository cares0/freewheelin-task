package freewheelin.pieceservice.application.service

import freewheelin.pieceservice.application.dto.CreatePieceCommand
import freewheelin.pieceservice.application.dto.PublishPieceCommand
import freewheelin.pieceservice.application.port.inbound.CreatePieceUseCase
import freewheelin.pieceservice.application.port.inbound.PublishPieceUseCase
import freewheelin.pieceservice.application.port.outbound.PieceLoadPort
import freewheelin.pieceservice.application.port.outbound.PieceSavePort
import freewheelin.pieceservice.application.port.outbound.ProblemLoadPort
import freewheelin.pieceservice.domain.Piece
import org.springframework.stereotype.Service

@Service
class PieceService(
    private val problemLoadPort: ProblemLoadPort,
    private val pieceSavePort: PieceSavePort,
    private val pieceLoadPort: PieceLoadPort,
) : CreatePieceUseCase,
    PublishPieceUseCase
{

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

    override fun publish(command: PublishPieceCommand) {
        val piece = pieceLoadPort.loadById(command.pieceId)
        piece.publishBatch(command.studentIds)
    }

}