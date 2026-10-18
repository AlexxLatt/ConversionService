package dev.fileconversion.service;


import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileListenerService {

    private final InboxRepository inBoxRepository;
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
