package dev.fileconversion.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.service.FileListenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class FileConversionListener {

    private static final String TOPIC_NAME = "file-conversion-requests";
    private final FileListenerService fileConversionListener;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "file-conversion-group"
    )

    public void consume(FileConversionRequestEvent event)  {
        log.info("Получено сообщение из Kafka, передаем в listener: {}", event.eventId());
        fileConversionListener.listen(event);
    }


}
