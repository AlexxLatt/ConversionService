package dev.fileconversion.event;


import java.util.UUID;

public record FileConversionResultEvent(
        String eventId,
        UUID eventKey,
        String status,
        String resultFilePath,
        String errorMessage
) {
}
