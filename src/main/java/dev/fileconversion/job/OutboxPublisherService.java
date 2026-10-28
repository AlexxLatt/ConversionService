package dev.fileconversion.job;


import dev.fileconversion.entity.Outbox;
import dev.fileconversion.event.FileConversionResultEvent;
import dev.fileconversion.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxPublisherService {


    private final OutboxRepository outBoxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC_NAME = "file-conversion-result";

    @Scheduled(fixedDelay = 5000)
    public void publish() {
        log.info("OutboxPublisher запущен");
        List<Outbox> outBoxList = outBoxRepository.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();

        for (Outbox event : outBoxList) {
            try {
                kafkaTemplate.send(
                        TOPIC_NAME,
                        event.getEventKey() == null ? null : event.getEventKey().toString(),
                        event.getPayload()
                ).get();

                event.setPublishedAt(Instant.now());

                outBoxRepository.save(event);

                log.info("Событие отправлено в Kafka: {}", event.getEventId());

            } catch (Exception e) {
                log.error(
                        "Ошибка отправки события {}",
                        event.getEventId(),
                        e
                );
            }
        }

    }

}
