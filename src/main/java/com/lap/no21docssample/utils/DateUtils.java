package com.lap.no21docssample.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateUtils {

    // Danh sách các định dạng ngày có thể kiểm tra
    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    );


    // Định dạng đích: "2025年8月6日"
    private static final DateTimeFormatter TARGET_FORMAT = DateTimeFormatter.ofPattern("yyyy年M月d日");

    public static String convertToJapaneseFormatIfDate(String input) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                return date.format(TARGET_FORMAT);
            } catch (DateTimeParseException e) {
                // ignore and try next formatter
            }
        }
        return input; // Không phải date
    }

}
