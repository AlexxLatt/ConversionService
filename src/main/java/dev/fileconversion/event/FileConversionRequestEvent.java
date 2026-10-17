package dev.fileconversion.event;

import java.time.Instant;

public record FileConversionRequestEvent(
   Long eventId,
   String sourceFilePath,
   Instant requestedAt,
   Instant createdAt,
   Instant sentAt
) {
}
