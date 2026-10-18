package dev.fileconversion.service;


import dev.fileconversion.converter.FileConversionStrategy;
import dev.fileconversion.entity.Outbox;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.exception.BadRequestException;
import dev.fileconversion.exception.NotFoundException;
import dev.fileconversion.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileConversionService {

    private final List<FileConversionStrategy> typeConversion;
    private final MinioService minioService;
    private final OutBoxRepository outBoxRepository;
    private final OutboxService outboxService;

    public void convert(FileConversionRequestEvent event) {


        if (outBoxRepository.existsById(event.eventId())) {
            log.info("Записть с  id:{}", event.eventId() + "уже существует");
            return;
        }

        String extension = FilenameUtils.getExtension(event.sourceFilePath());

        FileConversionStrategy format = typeConversion.stream()
                .filter(type -> type.supports(extension))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Не поддерживаемый тип конвертации: " + extension));

        String resultFilePath = event.eventId() + ".pdf";

        try (InputStream input = minioService.downloadFile(event.sourceFilePath());
             PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            format.convert(input, document);
            document.save(output);

            try (InputStream pdfInput = new ByteArrayInputStream(output.toByteArray())) {

                minioService.uploadFile(
                        resultFilePath,
                        pdfInput,
                        output.size()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка конвертации файла", e);
        }

        outboxService.saveResult(event, resultFilePath);

    }
}
