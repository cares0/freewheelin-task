package freewheelin.pieceservice.domain.exception

class PieceProblemCountExceedException(
    maximumCount: Int,
) : IllegalStateException(
    "학습지의 문제 수가 초과되었습니다. 최대 가능한 문제 수는 ${maximumCount}개 입니다."
)