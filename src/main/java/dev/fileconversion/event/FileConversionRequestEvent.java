package dev.fileconversion.event;



import java.time.Instant;
import java.util.UUID;


public record FileConversionRequestEvent(
   UUID eventId,
   String sourceFilePath,
   Instant requestedAt,
   Instant createdAt,
   Instant sentAt
) {
}
