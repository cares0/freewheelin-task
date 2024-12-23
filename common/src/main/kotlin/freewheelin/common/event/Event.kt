package freewheelin.common.event

import java.util.*

abstract class Event(
    val eventId: String = UUID.randomUUID().toString()
)