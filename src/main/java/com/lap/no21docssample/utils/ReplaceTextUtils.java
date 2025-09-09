package com.lap.no21docssample.utils;


import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReplaceTextUtils {

    public static void replacePlaceholdersInParagraph(XWPFDocument document, XWPFParagraph para, Map<String, String> replacements, Pattern pattern, Map<String, String> objectWithDefaultData, Map<String, String[][]> tableData, Map<String, String> objectWithLabel, Map<String, String> shortKeys) {
        List<XWPFRun> paragraphRuns = new ArrayList<>(para.getRuns());
        if (paragraphRuns.isEmpty()) return;
        // Ghép toàn bộ text của các run lại
        StringBuilder concatenatedText = new StringBuilder();
        Map<Integer, int[]> runWithPositionsOldAndNew = new HashMap<>();
        int runCount = paragraphRuns.size();
        int[] runEndIndices = new int[runCount];
        for (int i = 0; i < runCount; i++) {
            String t = paragraphRuns.get(i).getText(0);
            if (t == null) t = "";
            concatenatedText.append(t);
            runEndIndices[i] = concatenatedText.length();
        }
        String text = concatenatedText.toString();
        Matcher matcher = pattern.matcher(text);
        // Nếu không có placeholder thì return
        if (!matcher.find()) return;
        // Tìm tất cả các placeholder
        List<int[]> placeholderCharRanges = new ArrayList<>();
        matcher.reset();
        while (matcher.find()) {


            placeholderCharRanges.add(new int[]{matcher.start(), matcher.end(), matcher.start(1), matcher.end(1)});

        }
        String[] newTextByRunIndex = new String[runCount];
        // Nếu không có thì return
        if (placeholderCharRanges.isEmpty()) return;

        for (int[] range : placeholderCharRanges) {
            String key = text.substring(range[2], range[3]);
            //M007-02_KEY3

            String replacement = getReplacementForKey(key, replacements, objectWithDefaultData, objectWithLabel, shortKeys);
            replacement = createTableForKeyIfPresent(para, tableData, key, replacement);
            //_____________________


            // Xác định run bắt đầu và kết thúc bao phủ placeholder
            int startChar = range[0];
            int endChar = range[1];
            int startRunIdx = findRunIndexForChar(runEndIndices, startChar);
            int endRunIdx = findRunIndexForChar(runEndIndices, endChar - 1); // end is exclusive

            int startRunStartPos = startRunIdx == 0 ? 0 : runEndIndices[startRunIdx - 1];
            int endRunStartPos = endRunIdx == 0 ? 0 : runEndIndices[endRunIdx - 1];

            String startRunText = paragraphRuns.get(startRunIdx).getText(0);
            if (startRunText == null) startRunText = "";
            String endRunText = paragraphRuns.get(endRunIdx).getText(0);
            if (endRunText == null) endRunText = "";

            String prefixInStartRun = startRunText.substring(0, Math.max(0, startChar - startRunStartPos));
            String suffixInEndRun = endRunText.substring(Math.max(0, endChar - endRunStartPos));

            if (endRunIdx == startRunIdx) {
                if (newTextByRunIndex[startRunIdx] != null) {
                    int[] oldAndNewPos = runWithPositionsOldAndNew.get(startRunIdx);

                    newTextByRunIndex[startRunIdx] = newTextByRunIndex[startRunIdx].substring(0, oldAndNewPos[1]) + prefixInStartRun.substring(oldAndNewPos[0]) + replacement + suffixInEndRun;
                } else {
                    String startRunTextReplace = prefixInStartRun + replacement;
                    runWithPositionsOldAndNew.put(startRunIdx, new int[]{Math.max(0, endChar - endRunStartPos), startRunTextReplace.length()});
                    // Placeholder trong một run
                    newTextByRunIndex[startRunIdx] = startRunTextReplace + suffixInEndRun;
                }

            } else {
                // Ghi replacement vào run bắt đầu, giữ CTRPr của run bắt đầu
                if (newTextByRunIndex[startRunIdx] != null) {
                    int[] oldAndNewPos = runWithPositionsOldAndNew.get(startRunIdx);

                    newTextByRunIndex[startRunIdx] = prefixInStartRun.substring(oldAndNewPos[0]) + replacement;
                } else {
                    newTextByRunIndex[startRunIdx] = prefixInStartRun + replacement;
                }
                // Xóa nội dung các run ở giữa
                for (int i = startRunIdx + 1; i < endRunIdx; i++) {
                    newTextByRunIndex[i] = "";
                }
                runWithPositionsOldAndNew.put(endRunIdx, new int[]{Math.max(0, endChar - endRunStartPos)});
                // Giữ phần đuôi của run kết thúc
                newTextByRunIndex[endRunIdx] = suffixInEndRun;
            }
        }

        // Điền lại những run không bị ảnh hưởng
        for (int i = 0; i < runCount; i++) {
            if (newTextByRunIndex[i] == null) {
                String t = paragraphRuns.get(i).getText(0);
                newTextByRunIndex[i] = t == null ? "" : t;
            }
        }

       // Trong hàm xử lý (ví dụ replacePlaceholdersInParagraph)
//        for (int i = 0; i < runCount; i++) {
//            String replacement = newTextByRunIndex[i];
//            if (replacement == null) replacement = "";
//            String[] lines = replacement.split("\\\\n|\\n");
//            for (int j = 0; j < lines.length; j++) {
//                if (j > 0) paragraphRuns.get(i).addBreak();
//                paragraphRuns.get(i).setText(lines[j], j == 0 ? 0 : -1);
//            }
//        }
        for (int i = 0; i < paragraphRuns.size(); i++) {
            String replacement = newTextByRunIndex[i];
            if (replacement == null) replacement = "";
            String[] lines = replacement.split("\\\\n|\\n");
            if (lines.length == 0) lines = new String[]{""};

            XWPFRun baseRun = paragraphRuns.get(i);

            XWPFDocument doc = para.getDocument();

            // 1) Dòng đầu: thay trực tiếp trong run gốc
            baseRun.setText(lines[0], 0);

            // Nếu paragraph có numbering thì ghi nhận để copy cho các paragraph mới
            BigInteger numId = para.getNumID();
            BigInteger ilvl  = para.getNumIlvl();

            // 2) Các dòng còn lại: mỗi dòng = 1 paragraph mới, chèn đúng "sau" para hiện tại
            for (int j = 1; j < lines.length; j++) {
                XWPFParagraph newPara = insertParagraphAfter(para, doc);

                // defensive null-check (shouldn't happen thanks to fallback)
                if (newPara == null) {
                    throw new IllegalStateException("Cannot create new paragraph after the current paragraph.");
                }

                // Copy paragraph properties (indent/spacing/numPr…)
                if (para.getCTP().isSetPPr()) {
                    CTPPr src = para.getCTP().getPPr();
                    CTPPr dst = newPara.getCTP().isSetPPr() ? newPara.getCTP().getPPr() : newPara.getCTP().addNewPPr();
                    dst.set(src);
                }

                // Nếu đang ở list/bullets, giữ nguyên numId/ilvl
                if (numId != null) {
                    newPara.setNumID(numId);
                    if (ilvl != null) newPara.setNumILvl(ilvl);
                }

                // Copy run style
                XWPFRun newRun = newPara.createRun();
                if (baseRun.getCTR().isSetRPr()) {
                    CTRPr srcR = baseRun.getCTR().getRPr();
                    CTRPr dstR = newRun.getCTR().isSetRPr() ? newRun.getCTR().getRPr() : newRun.getCTR().addNewRPr();
                    dstR.set(srcR);
                }
                newRun.setText(lines[j]);

                // Cập nhật "para" để lần chèn sau sẽ nằm đúng ngay sau paragraph mới này
                para = newPara;
            }
        }



    }
    private static XWPFParagraph insertParagraphAfter(XWPFParagraph para, XWPFDocument doc) {
        XmlCursor cursor = para.getCTP().newCursor();
        XWPFParagraph newPara = null;
        try {
            cursor.toEndToken();
            cursor.toNextToken(); // move cursor *after* the paragraph node
            IBody body = para.getBody();
            if (body instanceof XWPFTableCell) {
                XWPFTableCell cell = (XWPFTableCell) body;
                // insert inside the same cell
                newPara = cell.insertNewParagraph(cursor); // may return null sometimes
            } else {
                // insert into document body
                newPara = doc.insertNewParagraph(cursor); // may return null sometimes
            }
        } finally {
            cursor.dispose();
        }

        // fallback if insertNewParagraph returned null for any reason
        if (newPara == null) {
            IBody body = para.getBody();
            if (body instanceof XWPFTableCell) {
                newPara = ((XWPFTableCell) body).addParagraph(); // append to cell
            } else {
                newPara = doc.createParagraph(); // append to document end (fallback)
            }
        }
        return newPara;
    }

    private static String getReplacementForKey(String key, Map<String, String> replacements, Map<String, String> objectWithDefaultData,  Map<String, String> objectWithLabel, Map<String, String> shortKeys) {
        boolean isKeyWithLabel = objectWithLabel.containsKey(key);
        String specialData = "";
        String extractedLabelKey = key ;
        if (isKeyWithLabel) {
            extractedLabelKey = key.contains(":") ? key.substring(key.lastIndexOf(':') + 1) : key;
            key = objectWithLabel.get(key);
        }
        boolean isKeyWithShortKey = shortKeys.containsKey(extractedLabelKey);
        if (isKeyWithShortKey) {
            if (ConstantUtils.SPECIAL_KEY_LIST.contains(extractedLabelKey)){
                specialData = ConstantUtils.SPECIAL_DATA;
            }
            extractedLabelKey = shortKeys.get(extractedLabelKey);
        }
        String replacement = replacements.get(extractedLabelKey);
        if (replacement == null) {
            String extractedKey = extractedLabelKey.contains(".") ? extractedLabelKey.substring(extractedLabelKey.lastIndexOf('.') + 1) : extractedLabelKey;
            replacement = objectWithDefaultData.containsKey(extractedKey)? objectWithDefaultData.get(extractedKey) : objectWithDefaultData.getOrDefault(extractedLabelKey,ConstantUtils.STRING_NOT_FOUND);
        }
        if (isKeyWithLabel){
            replacement = String.format(key, replacement);
        }
        if (ConstantUtils.SPECIAL_KEY_LIST.contains(extractedLabelKey) && replacement !=null && !replacement.isEmpty()){
            return replacement.concat(specialData);
        }
        return replacement;
    }



    private static int findRunIndexForChar(int[] runEndIndices, int charPos) {
        int low = 0;
        int high = runEndIndices.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (charPos < runEndIndices[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low < runEndIndices.length ? low : runEndIndices.length - 1;
    }

    private static String createTableForKeyIfPresent(XWPFParagraph para, Map<String, String[][]> tableData, String key, String replacement) {
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


    public static void replacePlaceholdersInTable(XWPFDocument document,XWPFTable table, Map<String, String> replacements, Pattern pattern, Map<String, String> objectWithDefaultData,  Map<String, String[][]> tableData, Map<String, String> objectWithLabel, Map<String, String> shortKeys) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                List<XWPFParagraph> paragraphsToProcess = new ArrayList<>(cell.getParagraphs());
                for (XWPFParagraph para : paragraphsToProcess) {
                    replacePlaceholdersInParagraph(document,para, replacements, pattern, objectWithDefaultData,tableData, objectWithLabel, shortKeys);
                }
            }
        }
    }

    // Hàm điền dữ liệu vào bảng
    private static   void populateTableCells(XWPFTable table, String[][] data) {
        for (int i = 0; i < data.length; i++) {
            XWPFTableRow row = table.getRow(i);
            for (int j = 0; j < data[i].length; j++) {
                row.getCell(j).setText(data[i][j]);
            }
        }
    }
}
