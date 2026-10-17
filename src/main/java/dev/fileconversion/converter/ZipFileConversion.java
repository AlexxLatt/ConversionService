package dev.fileconversion.converter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ZipFileConversion implements FileConversionStrategy {

    private final List<FileConversionStrategy> strategies;

    @Override
    public boolean supports(String fileName) {
        return "zip".equalsIgnoreCase(fileName);
    }

    @Override
    public void convert(InputStream input, PDDocument document) throws Exception {

        try (ZipInputStream zip =  new ZipInputStream(input, java.nio.charset.Charset.forName("CP866"))) {

            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    zip.closeEntry();
                    continue;
                }

                String entryName = FileNameUtils.getExtension(entry.getName());
                log.info("entryName: {}", entryName);

                FileConversionStrategy strategy = strategies.stream()
                        .filter(s -> s != this)
                        .filter(s -> s.supports(entryName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Неподдерживаемый файл в ZIP: " + entryName
                        ));

                log.info("Выбрана стратегия: {}", strategy.getClass().getSimpleName());

                strategy.convert(zip, document);

                zip.closeEntry();
            }
        }
    }
}
