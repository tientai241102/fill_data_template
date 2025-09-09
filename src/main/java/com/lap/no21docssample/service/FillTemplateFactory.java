package com.lap.no21docssample.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.lap.no21docssample.utils.FlatObjectUtils.flattenObjectToKeyValueMap;
import static com.lap.no21docssample.utils.ReplaceTextUtils.replacePlaceholdersInParagraph;
import static com.lap.no21docssample.utils.ReplaceTextUtils.replacePlaceholdersInTable;

public abstract class FillTemplateFactory {
    static String buildRadioOptionsWithMarks(String field, String selectedValue, Map<String, String[]> radioButtonOption) {
        StringBuilder result = new StringBuilder();

        String[] options = radioButtonOption.get(field);
        if (options == null) {
            return "Unknown field: " + field;
        }

        for (int i = 0; i < options.length; i++) {
            String indexStr = String.valueOf(i);
            result.append(indexStr.equals(selectedValue) ? "☑ " : "☐ ").append(" ").append(options[i])
                    .append("\n");
        }

        return result.toString();
    }

    void applyRadioButtonFormatting(Map<String, String> replacements, Map<String, String> radioButtonData, Map<String, String[]> radioButtonOption, Map<String, String[]> radioButtonReplaceData) {
        for (Map.Entry<String, String> entry : radioButtonData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String extractedKey = key.contains(".") ? key.substring(key.lastIndexOf('.') + 1) : key;
            String newValue = buildRadioOptionsWithMarks(extractedKey, value, radioButtonOption);
            String prefix = key.contains(".") ? key.substring(0, key.lastIndexOf('.') + 1) : key;
            // Nếu extractedKey nằm trong RADIO_BUTTON_REPLACE_DATA
            if (radioButtonReplaceData.containsKey(extractedKey)) {
                String[] relatedFields = radioButtonReplaceData.get(extractedKey);

                // Lấy các giá trị liên quan từ replacements, nếu không có thì ""
                Object[] relatedValues = new Object[relatedFields.length];
                for (int i = 0; i < relatedFields.length; i++) {
                    String fullKey = prefix + relatedFields[i];
                    relatedValues[i] = replacements.getOrDefault(fullKey, "");
                }

                // Format newValue với các giá trị liên quan
                newValue = String.format(newValue, relatedValues);
            }

            replacements.put(key, newValue);

        }
    }

    protected Map<String, String> buildReplacementMap(Map<String, Object> data,
                                                      Map<String, String> objectWithDataType,
                                                      Map<String, String> objectWithLabel,
                                                      Map<String, String[]> radioButtonOptions,
                                                      Map<String, String[]> radioButtonReplaceData,
                                                      Map<String, String[][]> tableData) {
        Map<String, String> replacements = new HashMap<>();
        Map<String, String> radioButtonData = new HashMap<>();
        flattenObjectToKeyValueMap("", data, replacements, objectWithDataType, radioButtonData, objectWithLabel,tableData);

        replacements.put("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年M月d日")));
        applyRadioButtonFormatting(replacements, radioButtonData, radioButtonOptions, radioButtonReplaceData);
        return replacements;
    }

    protected byte[] exportDataToWordFile(InputStream templateStream, Map<String, String> replacements, Map<String, String> objectWithDefaultData,   Map<String, String[][]> tableData, Map<String, String> objectWithLabel, Map<String, String> shortKeys) throws IOException {
        // Tìm và thay thế tất cả placeholder trong tài liệu
        XWPFDocument doc = new XWPFDocument(templateStream);
        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");

        // Thu thập list copy trước khi xử lý
        List<XWPFParagraph> paragraphsToProcess = new ArrayList<>(doc.getParagraphs());
        for (XWPFParagraph para : paragraphsToProcess) {
            replacePlaceholdersInParagraph(doc,para, replacements, pattern, objectWithDefaultData,tableData, objectWithLabel, shortKeys);
        }
        List<XWPFTable> paragraphsTableToProcess = new ArrayList<>(doc.getTables());
        for (XWPFTable table : paragraphsTableToProcess) {
            replacePlaceholdersInTable(doc,table, replacements, pattern, objectWithDefaultData,tableData, objectWithLabel, shortKeys);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        doc.close();
        return out.toByteArray();
    }

    public abstract byte[] fillDocxTemplate(String url) throws IOException;

    public abstract byte[] fillDocxTemplateFile(MultipartFile file) throws IOException;

}
