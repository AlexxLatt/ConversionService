package dev.fileconversion.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import dev.fileconversion.converter.FileConversionStrategy;
import dev.fileconversion.entity.OutBox;
import dev.fileconversion.event.FileConversionRequestEvent;
import dev.fileconversion.event.FileConversionResultEvent;
import dev.fileconversion.exception.BadRequestException;
import dev.fileconversion.exception.NotFoundException;
import dev.fileconversion.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileConversionService {

    private final List<FileConversionStrategy> typeConversion;
    private final MinioService minioService;
    private final OutBoxRepository outBoxRepository;
    private final ObjectMapper objectMapper;

    public void convert(FileConversionRequestEvent event) {


        if (outBoxRepository.existsById(event.eventId())){
            OutBox outBox = outBoxRepository.findById(event.eventId())
                    .orElseThrow(()-> new NotFoundException("Ошибка поиска по id: "+ event.eventId()));

        }


        String extension = FilenameUtils.getExtension(event.sourceFilePath());

        FileConversionStrategy format  = typeConversion.stream()
                .filter(type-> type.supports(extension))
                .findFirst()
                .orElseThrow(()-> new BadRequestException("Не поддерживаемый тип конвертации: " + extension));

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
        }catch (Exception e) {
            throw new RuntimeException("Ошибка конвертации файла", e);
        }

        FileConversionResultEvent result = new FileConversionResultEvent(
                UUID.randomUUID().toString(),
                event.eventId(),
                "SUCCESS",
                resultFilePath,
                null
        );

        OutBox outBox = new OutBox();outBox.setEventId(result.eventId());
        outBox.setEventKey(result.eventKey());
        outBox.setPayload(objectMapper.writeValueAsString(result));
        outBox.setCreatedAt(Instant.now());
        outBox.setPublishedAt(null);

        outBoxRepository.save(outBox);

    }

}
