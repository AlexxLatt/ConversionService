package dev.fileconversion.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResultConsumer {

    private final String TOPIC_NAME = "file-conversion-result";

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "file-conversion-group"
    )
    public void consume(String message){
        log.info("Получен результат {} ",message);

    }
}
