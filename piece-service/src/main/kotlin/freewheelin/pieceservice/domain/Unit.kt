package freewheelin.pieceservice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Unit(
    @Id
    @Column(name = "code")
    val code: String,
    val name: String,
    val index: Int,
)