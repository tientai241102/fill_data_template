package com.lap.no21docssample.utils;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ReplaceTextUtilsOld {

    public static void replaceInParagraph(XWPFDocument document, XWPFParagraph para, Map<String, String> replacements, Pattern pattern, Map<String, String> objectWithDefaultData, Map<String, String[][]> tableData) {
        List<XWPFRun> runs = new ArrayList<>(para.getRuns());
        if (runs.isEmpty()) return;
        // Ghép toàn bộ text của các run lại
        StringBuilder fullText = new StringBuilder();
        List<Integer> runTextLengths = new ArrayList<>();
        Map<Integer, Integer> charIndexToRunIndex = new HashMap<>();
        for (XWPFRun run : runs) {
            String t = run.getText(0);
            if (t == null) t = "";
            fullText.append(t);
            runTextLengths.add(t.length());
            charIndexToRunIndex.put(runs.indexOf(run), fullText.length());
        }
        String text = fullText.toString();
        Matcher matcher = pattern.matcher(text);
        // Nếu không có placeholder thì return
        if (!matcher.find()) return;
        // Lưu lại các vị trí bắt đầu/kết thúc của từng run trong fullText
        List<int[]> runRanges = new ArrayList<>();
        int pos = 0;
        for (int len : runTextLengths) {
            runRanges.add(new int[]{pos, pos + len});
            pos += len;
        }
        // Tìm tất cả các placeholder
        List<int[]> placeholderRanges = new ArrayList<>();
        matcher.reset();
        while (matcher.find()) {


            placeholderRanges.add(new int[]{matcher.start(), matcher.end(), matcher.start(1), matcher.end(1)});

        }
        Map<Integer, String> runWithNewString = new HashMap<>();
        // Nếu không có thì return
        if (placeholderRanges.isEmpty()) return;
        // Tạo text mới đã thay thế
        StringBuilder replacedText = new StringBuilder();
        int last = 0;
        int startRunIndex = 0;

        for (int[] range : placeholderRanges) {
            for (int i = startRunIndex; i < charIndexToRunIndex.size(); i++) {
                String key = text.substring(range[2], range[3]);

                String extractedKey = key.contains(".") ? key.substring(key.lastIndexOf('.') + 1) : key;
                String replacement = replacements.get(key);
                if (replacement == null) {

                    replacement = objectWithDefaultData.getOrDefault(extractedKey, ConstantUtils.STRING_NOT_FOUND);
                }


                if (range[0] < charIndexToRunIndex.get(i)) {
                    if (i != 0 && charIndexToRunIndex.get(i - 1) < range[1] && range[1] <= charIndexToRunIndex.get(i)) {
                        replacement = createTable(para, tableData, key, replacement);
                        String newText = replacement.concat(runs.get(i).getText(0).substring(range[1] - charIndexToRunIndex.get(i - 1)));

                        runWithNewString.put(i, newText);
                        if (range[1] <= charIndexToRunIndex.get(i)) {
                            startRunIndex = ++i;
                        }
                        // Nếu placeholder nằm trong run này thì không cần thay đổi startRunIndex
                        break;
                    } else if (i != 0 && charIndexToRunIndex.get(i) < range[1]) {
                        runWithNewString.put(i, "");
                    } else if (i == 0) {
                        if (range[1] == charIndexToRunIndex.get(i)) {
                            replacement = createTable(para, tableData, key, replacement);
                            runWithNewString.put(i, replacement);
                        } else {
                            runWithNewString.put(i, runs.get(i).getText(0).substring(0, range[0]));
                        }
                    }
                    // Nếu placeholder nằm ở cuối run này thì cần thay đổi startRunIndex


                } else {
                    runWithNewString.put(i, runs.get(i).getText(0));
                }
                startRunIndex = i;
            }

        }
        startRunIndex++;
        for (int i = startRunIndex; i < charIndexToRunIndex.size(); i++) {
            if (charIndexToRunIndex.get(i) != null) continue;
            runWithNewString.put(i, runs.get(i).getText(0));
        }
        replacedText.append(text.substring(last));

        for (int i = 0; i < runWithNewString.size(); i++) {
            // Nếu replacement có nhiều dòng thì addBreak
            String replacement = runWithNewString.get(i);
            if (replacement == null) replacement = "";
            String[] lines = replacement.split("\\\\n|\\n");
            for (int j = 0; j < lines.length; j++) {
                if (j > 0) runs.get(i).addBreak();
                runs.get(i).setText(lines[j], j == 0 ? 0 : -1);
            }

        }
    }

    private static String createTable(XWPFParagraph para, Map<String, String[][]> tableData, String key, String replacement) {
        if (tableData.containsKey(key)){

            String[][] data = tableData.get(key);

            // Tạo con trỏ ngay tại paragraph
            XmlCursor cursor = para.getCTP().newCursor();

            // Tạo bảng
            XWPFTable newTable = para.getDocument().insertNewTbl(cursor);

            // Thêm dữ liệu
            for (int k = 0; k < data.length; k++) {
                XWPFTableRow row;
                if (k == 0) {
                    row = newTable.getRow(0); // hàng đầu tiên đã có sẵn
                } else {
                    row = newTable.createRow();
                }
                for (int j = 0; j < data[k].length; j++) {
                    if (k == 0 && j > 0) {
                        row.createCell();
                    }
                    row.getCell(j).setText(data[k][j] != null ? data[k][j] : "");
                }
            }
            replacement ="";
        }
        return replacement;
    }


    public static void replaceInTable(XWPFDocument document,XWPFTable table, Map<String, String> replacements, Pattern pattern, Map<String, String> objectWithDefaultData,  Map<String, String[][]> tableData) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (XWPFParagraph para : cell.getParagraphs()) {
                    replaceInParagraph(document,para, replacements, pattern, objectWithDefaultData,tableData);
                }
            }
        }
    }

    // Hàm điền dữ liệu vào bảng
    private static   void fillTable(XWPFTable table, String[][] data) {
        for (int i = 0; i < data.length; i++) {
            XWPFTableRow row = table.getRow(i);
            for (int j = 0; j < data[i].length; j++) {
                row.getCell(j).setText(data[i][j]);
            }
        }
    }
}
