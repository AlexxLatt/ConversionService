package dev.fileconversion.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class TxtFileConversion implements FileConversionStrategy {

    @Override
    public boolean supports(String fileType) {
        return "TXT".equalsIgnoreCase(fileType);
    }

    @Override
    public void convert(InputStream input, PDDocument document) throws Exception {

        String text = new String(
                input.readAllBytes(),
                StandardCharsets.UTF_8
        );

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (InputStream fontStream = getClass().getResourceAsStream("/fonts/NotoSans-Regular.ttf")) {

            if (fontStream == null) {
                throw new IllegalStateException(
                        "Не найден шрифт /fonts/NotoSans-Regular.ttf"
                );
            }

            PDType0Font font = PDType0Font.load(document, fontStream);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {

                content.beginText();
                content.setFont(font, 12);
                content.newLineAtOffset(50, 780);

                for (String line : text.split("\\R", -1)) {
                    content.showText(line);
                    content.newLineAtOffset(0, -16);
                }

                content.endText();
            }

        }
    }
}
