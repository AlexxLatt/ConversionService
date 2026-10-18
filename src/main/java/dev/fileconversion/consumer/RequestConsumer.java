package dev.fileconversion.consumer;

import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.listener.FileConversionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;


@Slf4j
@Component
@RequiredArgsConstructor
public class RequestConsumer {

    private static final String TOPIC_NAME = "file-conversion-requests";

    private final FileConversionListener fileConversionListener;


    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "file-conversion-group"
    )

    public void consume(FileConversionRequestEvent event) {
        log.info("Получено сообщение из Kafka, передаем в listener: {}", event.eventId());
        fileConversionListener.listen(event);
    }


}
