package freewheelin.pieceservice.adapter.driven.event

import freewheelin.common.event.Event
import freewheelin.common.supports.logger
import freewheelin.pieceservice.application.port.outbound.PieceEventPublishPort
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
) : PieceEventPublishPort {

    val log = logger()

    override fun publish(event: Event) {
        log.info("publish event $event")
        applicationEventPublisher.publishEvent(event)
    }

}