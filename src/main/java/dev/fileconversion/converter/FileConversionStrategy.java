package dev.fileconversion.converter;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.InputStream;

public interface FileConversionStrategy {

    boolean supports(String fileType);
    void convert(InputStream input, PDDocument document)
            throws Exception;
}
