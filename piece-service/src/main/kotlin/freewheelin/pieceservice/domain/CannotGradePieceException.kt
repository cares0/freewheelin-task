package freewheelin.pieceservice.domain

class CannotGradePieceException : IllegalStateException("채점할 수 없는 문제입니다.") {
}