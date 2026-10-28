package dev.fileconversion.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Slf4j
@Service
public class ImageFileConversion implements FileConversionStrategy {

    @Override
    public boolean supports(String fileName) {
        String name = fileName.toLowerCase();

        return name.equalsIgnoreCase("png")
                || name.equalsIgnoreCase("jpg")
                || name.equalsIgnoreCase("jpeg");
    }

    @Override
    public void convert(InputStream input, PDDocument document) throws Exception {

        BufferedImage image = ImageIO.read(input);

        if (image == null) {
            throw new IllegalArgumentException(
                    "Не удалось прочитать изображение"
            );
        }

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDImageXObject pdfImage =
                LosslessFactory.createFromImage(document, image);

        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();

        float scale = Math.min(
                pageWidth / image.getWidth(),
                pageHeight / image.getHeight()
        );

        float width = image.getWidth() * scale;
        float height = image.getHeight() * scale;

        float x = (pageWidth - width) / 2;
        float y = (pageHeight - height) / 2;

        try (PDPageContentStream content =
                     new PDPageContentStream(document, page)) {

            content.drawImage(pdfImage, x, y, width, height);
        }
    }
}
