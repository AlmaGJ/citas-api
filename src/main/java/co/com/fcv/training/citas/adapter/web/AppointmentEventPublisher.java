package co.com.fcv.training.citas.adapter.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestClient;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Optional S5 adapter. It is inert unless explicitly enabled and never changes appointment state on delivery failure. */
@Component
class AppointmentEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(AppointmentEventPublisher.class);
    private final RestClient client;
    private final boolean enabled;
    private final String url;
    private final String bearer;
    AppointmentEventPublisher(RestClient.Builder builder,
                              @Value("${app.n8n.webhook.enabled:false}") boolean enabled,
                              @Value("${app.n8n.webhook.url:}") String url,
                              @Value("${app.n8n.webhook.bearer-token:}") String bearer) {
        if (enabled && (url.isBlank() || bearer.isBlank())) throw new IllegalStateException("N8N webhook requiere URL y bearer token");
        this.client=builder.build();this.enabled=enabled;this.url=url;this.bearer=bearer;
    }
    void publish(long appointmentId,String status,String source,long actorId) {
        if(!enabled || !Set.of("APPROVED","REJECTED","CANCELLED").contains(status)) return;
        deliver(Map.of("schemaVersion","1","eventId",UUID.randomUUID().toString(),"eventType","AppointmentStatusChanged","appointmentId",appointmentId,"status",status,"source",source,"actorUserId",actorId,"occurredAt",Instant.now().toString()),appointmentId);
    }
    void publishRescheduleDecision(long appointmentId,long rescheduleRequestId,String status,long actorId) {
        if(!enabled || !Set.of("APPROVED","REJECTED").contains(status)) return;
        deliver(Map.of("schemaVersion","1","eventId",UUID.randomUUID().toString(),"eventType","AppointmentRescheduleDecided","appointmentId",appointmentId,"rescheduleRequestId",rescheduleRequestId,"status",status,"source","ADMIN","actorUserId",actorId,"occurredAt",Instant.now().toString()),appointmentId);
    }
    private void deliver(Map<String,Object> body,long appointmentId) {
        Runnable send=()->{try{client.post().uri(url).contentType(MediaType.APPLICATION_JSON).header("Authorization","Bearer "+bearer).body(body).retrieve().toBodilessEntity();}catch(Exception e){log.warn("No se pudo entregar evento de cita {}: {}",appointmentId,e.getClass().getSimpleName());}};
        if(TransactionSynchronizationManager.isSynchronizationActive()) TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){public void afterCommit(){send.run();}}); else send.run();
    }
}
