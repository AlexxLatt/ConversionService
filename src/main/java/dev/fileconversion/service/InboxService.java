package dev.fileconversion.service;

import dev.fileconversion.entity.Inbox;
import dev.fileconversion.enums.InboxStatus;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.repository.InboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inBoxRepository;

    @Transactional
    public void saveResult(FileConversionRequestEvent event) {
        inBoxRepository.save(
                new Inbox(event.eventId(), event.requestedAt(), InboxStatus.PROCESSED)
        );
        log.info("5. Событие сохранено в Inbox");
    }
}
