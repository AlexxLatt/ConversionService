package dev.fileconversion.service;


import dev.fileconversion.entity.Inbox;
import dev.fileconversion.entity.Outbox;
import dev.fileconversion.enums.InboxStatus;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.event.FileConversionResultEvent;
import dev.fileconversion.repository.InboxRepository;
import dev.fileconversion.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversionResultService {

    private  final InboxRepository inboxRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void saveConversionResult(FileConversionRequestEvent requestEvent, String resultFilePath) {

        Inbox inbox = new Inbox(
                requestEvent.eventId(),
                requestEvent.requestedAt(),
                InboxStatus.PROCESSED
        );
        inboxRepository.save(inbox);

        FileConversionResultEvent resultEvent = new FileConversionResultEvent(
                UUID.randomUUID().toString(),
                requestEvent.eventId(),
                "SUCCESS",
                resultFilePath,
                null
        );


        Outbox outbox = new Outbox();

        outbox.setEventId(resultEvent.eventId());

        outbox.setEventKey(requestEvent.eventId());
        outbox.setPayload(objectMapper.writeValueAsString(resultEvent));
        outbox.setCreatedAt(Instant.now());
        outbox.setPublishedAt(null);

        outboxRepository.save(outbox);

        log.info("Успешно сохранено в Inbox и Outbox в одной транзакции. Исходный eventId: {}", requestEvent.eventId());

    }
}
