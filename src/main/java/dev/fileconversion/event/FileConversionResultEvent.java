package dev.fileconversion.event;

import java.time.Instant;

public record FileConversionResultEvent(
        String eventId,
        Long eventKey,
        String status,
        String resultFilePath,
        String errorMessage
) {
}
