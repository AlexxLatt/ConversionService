package dev.fileconversion.service;


import dev.fileconversion.entity.Outbox;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.event.FileConversionResultEvent;
import dev.fileconversion.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outBoxRepository;
    private final ObjectMapper objectMapper;


    @Transactional
    public void saveResult(FileConversionRequestEvent event, String resultFilePath) {

        FileConversionResultEvent result = new FileConversionResultEvent(
                UUID.randomUUID().toString(),
                event.eventId(),
                "SUCCESS",
                resultFilePath,
                null
        );

        Outbox outBox = new Outbox();
        outBox.setEventId(result.eventId());
        outBox.setEventKey(result.eventKey());
        outBox.setPayload(objectMapper.writeValueAsString(result));
        outBox.setCreatedAt(Instant.now());
        outBox.setPublishedAt(null);

        outBoxRepository.save(outBox);


    }
}
