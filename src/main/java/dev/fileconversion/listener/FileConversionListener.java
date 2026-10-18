package dev.fileconversion.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.repository.InBoxRepository;
import dev.fileconversion.service.FileConversionService;
import dev.fileconversion.service.InboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileConversionListener {

    private final InBoxRepository inBoxRepository;
    private final FileConversionService fileConversionService;
    private final InboxService inboxService;

    public void listen(FileConversionRequestEvent event)  {

        log.info("1. Получено событие: eventId={}", event.eventId());

        if (inBoxRepository.existsById(event.eventId())) {
            log.info("2. Событие уже обработано: eventId={}", event.eventId());
            return;
        }

        log.info("3. Начинаем конвертацию");
        fileConversionService.convert(event);

        log.info("4. Конвертация завершилась");

        inboxService.saveResult(event);
    }


}
