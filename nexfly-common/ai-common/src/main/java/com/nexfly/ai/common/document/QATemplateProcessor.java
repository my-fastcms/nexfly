package com.nexfly.ai.common.document;

import org.apache.poi.ss.usermodel.*;
import org.springframework.ai.document.Document;
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
public class QATemplateProcessor implements DocumentTemplateProcessor {
    @Override
    public List<org.springframework.ai.document.Document> process(InputStream inputStream, String fileType) throws Exception {
        List<org.springframework.ai.document.Document> documents;

        switch (fileType.toLowerCase()) {
            case "application/vnd.ms-excel":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                // 解析 EXCEL
                documents = parseExcel(inputStream);
                break;
            case "text/csv":
            case "text/plain":
                // 解析 CSV 或 TXT
                documents = parseCSVOrTxt(inputStream);
                break;
            default:
                throw new Exception("Unsupported file type: " + fileType);
        }
        return documents;
    }

    // 示例：解析 CSV 或 TXT
    private List<Document> parseCSVOrTxt(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        Document document = new Document(content.toString());
        return List.of(document);
    }

    // 使用 Excel 解析器解析 EXCEL 文件
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
        Document document = new Document(content.toString());
        return List.of(document);
    }

}
