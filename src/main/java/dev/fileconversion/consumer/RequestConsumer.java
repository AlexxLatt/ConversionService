package dev.fileconversion.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fileconversion.service.FileConversionService;
import dev.fileconversion.entity.InBox;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.repository.InBoxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;


@Slf4j
@Component
@RequiredArgsConstructor
public class RequestConsumer {

    private final InBoxRepository inBoxRepository;
    private final FileConversionService fileConversionService;
    private final String TOPIC_NAME = "file-conversion-requests";
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "file-conversion-group"
    )


    @Transactional
    public void consume(String massage)throws JsonProcessingException {

        FileConversionRequestEvent event =  objectMapper.readValue(massage, FileConversionRequestEvent.class);

        log.info("1. Получено событие: eventId={}", event.eventId());

        if (inBoxRepository.existsById(event.eventId())) {
            log.info("2. Событие уже обработано: eventId={}", event.eventId());
            return;
        }

        log.info("3. Начинаем конвертацию");
        fileConversionService.convert(event);

        log.info("4. Конвертация завершилась");

        inBoxRepository.save(
                new InBox(event.eventId(), event.requestedAt())
        );

        log.info("5. Событие сохранено в Inbox");
    }




}
