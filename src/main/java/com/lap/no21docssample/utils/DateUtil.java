package com.lap.no21docssample.utils;


import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.lap.no21docssample.utils.DateTimeFormat.*;


public class DateUtil {

  public static String getJpDateStringFromDateObj(LocalDate date, String format) {
    if (!isValidFormat(format)) {
      throw new IllegalArgumentException("Invalid format: " + format);
    }

    Map<String, Object> jp_year = convertAdYearToJpYear(date);
    String era_type = (String) jp_year.getOrDefault("era_type", null);
    Integer year = (Integer) jp_year.getOrDefault("year", null);
    return getJpDateStringFromJpYear(
        era_type, year, date.getMonthValue(), date.getDayOfMonth(), format);
  }

  private static boolean isValidFormat(String format) {
    return JAPANESE_DATE_FORMAT_WITH_ERA.equals(format)
        || JAPANESE_DATE_FORMAT_MONTH_YEAR_WITH_ERA.equals(format)
        || JAPANESE_DATE_FORMAT_MONTH_YEAR_JES_WITH_ERA.equals(format);
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> convertAdYearToJpYear(LocalDate date) {
    LinkedHashMap<String, Object> conf = YearEraConstant.getValues();
    LinkedHashMap<String, Object> getYears =
        (LinkedHashMap<String, Object>) conf.getOrDefault("YEARS", null);

    Map<String, Object> result = new HashMap<>();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    for (Map.Entry<String, Object> entry : getYears.entrySet()) {
      LinkedHashMap<String, Object> row = (LinkedHashMap<String, Object>) entry.getValue();
      String id = (String) row.getOrDefault("ID", null);
      String from = (String) row.getOrDefault("FROM", null);
      String to = (String) row.getOrDefault("TO", null);
      String jp = (String) row.getOrDefault("JP", null);
      LocalDate fromDate = LocalDate.parse(from, dtf);
      LocalDate toDate = Strings.isBlank(to) ? null : LocalDate.parse(to, dtf);

      if (date.isBefore(fromDate)) continue;

      if (toDate == null) {
        int yearsBetween = date.getYear() - fromDate.getYear();
        result.put("era_type", id);
        result.put("era", jp);
        result.put("year", yearsBetween + 1);
        break;
      }

      if (toDate.isBefore(date)) continue;

      int yearsBetween = date.getYear() - fromDate.getYear();
      result.put("era_type", id);
      result.put("era", jp);
      result.put("year", yearsBetween + 1);
      break;
    }
    return result;
  }

  private static String getJpDateStringFromJpYear(
      String eraType, Integer jpYear, int month, int day, String format) {
    String era = getEra(eraType);
    LinkedHashMap<String, Object> defaultFormat = getDateFormatString(format);

    String formatY = defaultFormat.get("year").toString();
    String formatM = defaultFormat.get("month").toString();
    String formatD = defaultFormat.containsKey("day") ? defaultFormat.get("day").toString() : null;

    String result;
    if (formatD != null) {
      result = era + String.format(formatY + formatM + formatD, jpYear, month, day);
    } else {
      result = era + String.format(formatY + formatM, jpYear, month);
    }
    return result;
  }

  private static LinkedHashMap<String, Object> getDateFormatString(String format) {
    LinkedHashMap<String, Object> formatDate = new LinkedHashMap<>();
    if (format.equals(JAPANESE_DATE_FORMAT_WITH_ERA)) {
      formatDate.put("year", "%s年  ");
      formatDate.put("month", "%02d月  ");
      formatDate.put("day", "%02d日");
    } else if (format.equals(JAPANESE_DATE_FORMAT_MONTH_YEAR_WITH_ERA)) {
      formatDate.put("year", "%s年  ");
      formatDate.put("month", "%02d月頃");
    } else {
      formatDate.put("year", "%s年  ");
      formatDate.put("month", "%02d月");
    }
    return formatDate;
  }

  private static String getEra(String eraTypeID) {
    LinkedHashMap<String, Object> getJpYear =
        (LinkedHashMap<String, Object>) YearEraConstant.getValues().get("JP_YEAR");
    String era = (String) getJpYear.getOrDefault(eraTypeID, null);
    return Strings.isBlank(era) ? null : era;
  }

  // Danh sách các định dạng ngày có thể kiểm tra
  private static final List<DateTimeFormatter> FORMATTERS =
      List.of(
          DateTimeFormatter.ofPattern("yyyy-MM-dd"),
          DateTimeFormatter.ofPattern("dd/MM/yyyy"),
          DateTimeFormatter.ofPattern("MM/dd/yyyy"),
          DateTimeFormatter.ofPattern("yyyy/MM/dd"),
          DateTimeFormatter.ofPattern("dd-MM-yyyy"));

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
