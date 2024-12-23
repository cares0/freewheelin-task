package freewheelin.pieceservice.domain.model

import jakarta.persistence.*
import jakarta.persistence.FetchType.*

@Entity
class Problem(
    @Id
    @Column(name = "problem_id")
    val id: Long = 0,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_code")
    val unit: Unit,

    val level: Int,

    @Enumerated(EnumType.STRING)
    val type: ProblemType,

    val contents: String,

    val answer: String,
)