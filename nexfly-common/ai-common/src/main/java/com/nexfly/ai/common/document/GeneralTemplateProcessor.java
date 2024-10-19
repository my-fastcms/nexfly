package com.nexfly.ai.common.document;

import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/10/17
 **/
@Service
@Primary
public class GeneralTemplateProcessor implements DocumentTemplateProcessor {

    @Override
    public List<Document> process(InputStream inputStream, String fileType) throws Exception {
        List<Document> documents = new ArrayList<>();

        switch (fileType.toLowerCase()) {
            case "application/pdf":
                // PDF 文件解析
                documents = parsePDF(inputStream);
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            case "application/msword":
                // DOCX 文件解析
                documents = parseDOCX(inputStream);
                break;
            case "application/vnd.ms-excel":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                // EXCEL 文件解析
                documents = parseExcel(inputStream);
                break;
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                // PPT 文件解析
//                documents = parsePPT(inputStream);
                break;
            case "text/plain":
                // TXT 文件解析
                documents = parseText(inputStream);
                break;
            case "image/jpeg":
            case "image/png":
                // 图片文件处理，使用 OCR 提取文本
//                documents = parseImage(inputStream);
                break;
            default:
                throw new Exception("Unsupported file type: " + fileType);
        }
        return documents;
    }

    // 示例：解析 PDF
    private List<Document> parsePDF(InputStream inputStream) throws Exception {
        List<org.springframework.ai.document.Document> documents = new ArrayList<>();

        // 使用 PDFParser 解析 InputStream
        try (RandomAccessReadBuffer randomAccessReadBuffer = new RandomAccessReadBuffer(inputStream);
             PDDocument pdfDocument = new PDFParser(randomAccessReadBuffer).parse()) {

            // 使用 PDFTextStripper 提取文本
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(pdfDocument);

            // 将提取的文本转换为自定义的 Document 对象
            org.springframework.ai.document.Document document = new org.springframework.ai.document.Document(text);
            documents.add(document);

        } catch (Exception e) {
            throw new Exception("Failed to process PDF document", e);
        }

        return documents;
    }

    // 示例：解析 DOCX
    private List<Document> parseDOCX(InputStream inputStream) throws Exception {
        XWPFDocument docx = new XWPFDocument(inputStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
        String content = extractor.getText();
        // 创建文档对象并返回
        Document document = new Document(content);
        extractor.close();
        return List.of(document);
    }

    // 示例：解析 Excel
    private List<Document> parseExcel(InputStream inputStream) throws Exception {
        Workbook workbook = WorkbookFactory.create(inputStream);
        StringBuilder content = new StringBuilder();
        for (Sheet sheet : workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    content.append(cell.toString()).append(" ");
                }
                content.append("\n");
            }
        }
        workbook.close();
        // 创建文档对象并返回
        Document document = new Document(content.toString());
        return List.of(document);
    }

    // 示例：解析图片
    /*private List<Document> parseImage(InputStream inputStream) throws Exception {
        ITesseract tesseract = new Tesseract();
        BufferedImage image = ImageIO.read(inputStream);
        String result = tesseract.doOCR(image);
        // 将 OCR 结果转换为文档对象
        Document document = new Document(result);
        return List.of(document);
    }*/

    // 示例：解析文本文件
    private List<Document> parseText(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        // 创建文档对象并返回
        Document document = new Document(content.toString());
        return List.of(document);
    }

}
