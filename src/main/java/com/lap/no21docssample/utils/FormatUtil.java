package com.lap.no21docssample.utils;


import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.lap.no21docssample.utils.DateTimeFormat.*;


public class FormatUtil {
  public Object japaneseDateFormat(Object date) {
    if (ObjectUtils.isEmpty(date)) {
      return "年　 月 　日";
    }

    LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-M-d"));

    DateTimeFormatter format = DateTimeFormatter.ofPattern(JAPANESE_DATE_FORMAT);

    return localDate.format(format);
  }

  public Object japaneseDateFormat1(Object date) {
    if (ObjectUtils.isEmpty(date)) {
      return null;
    }

    LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-M-d"));

    DateTimeFormatter format = DateTimeFormatter.ofPattern(JAPANESE_DATE_FORMAT);

    return localDate.format(format);
  }

  public Object japaneseDateFormatMonthYear(Object date) {
    if (ObjectUtils.isEmpty(date)) {
      return "年　　月頃";
    }

    LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-M-d"));

    DateTimeFormatter format = DateTimeFormatter.ofPattern(JAPANESE_DATE_FORMAT_MONTH_YEAR);

    return localDate.format(format);
  }

  private Object createJapaneseMonthYearFormat(int flag, Object date, Object valueOfCheckbox) {
    boolean isSeireki = !ObjectUtils.isEmpty(valueOfCheckbox) && valueOfCheckbox.equals(true);

    LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-M-d"));

    DateTimeFormatter format;
    if (isSeireki) {
      if (flag == DateTimeStatusEnum.JES.value) {
        format = DateTimeFormatter.ofPattern(JAPANESE_DATE_FORMAT_MONTH_YEAR_JES);
      } else {
        format = DateTimeFormatter.ofPattern(JAPANESE_DATE_FORMAT_MONTH_YEAR);
      }
    } else {
      if (flag == DateTimeStatusEnum.JES.value) {
        String result =
            DateUtil.getJpDateStringFromDateObj(
                localDate, JAPANESE_DATE_FORMAT_MONTH_YEAR_JES_WITH_ERA);
        return result.replaceAll("  0", "&nbsp;&nbsp;");
      } else {
        String result =
            DateUtil.getJpDateStringFromDateObj(
                localDate, JAPANESE_DATE_FORMAT_MONTH_YEAR_WITH_ERA);
        return result.replaceAll("  0", "&nbsp;&nbsp;");
      }
    }
    return localDate.format(format).replaceAll("  0", "&nbsp;&nbsp;");
  }

  public Object japaneseMonthYear(Object date) {
    if (ObjectUtils.isEmpty(date)) {
      return "年　　月";
    }
    try {
      LocalDate localDate =
          LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-M-d"));

      int day = localDate.getDayOfMonth();
      int month = localDate.getMonthValue();
      int year = localDate.getYear();

      String yearLabel =
          (year < 1989 || (year == 1989 && month == 1 && day < 8))
              ? "昭和"
              : (year < 2019 || (year == 2019 && month < 5)) ? "平成" : "令和";

      int yearLabelNumber =
          yearLabel.equals("昭和")
              ? (year - 1926 + 1)
              : yearLabel.equals("平成") ? (year - 1989 + 1) : (year - 2019 + 1);

      String monthNumber = month < 10 ? ("0" + month) : String.valueOf(month);

      return String.format("%s %d 年 %s 月 ", yearLabel, yearLabelNumber, monthNumber);
    } catch (Exception ex) {
      return "年　　月";
    }
  }

  public Object japaneseMonthYear(int flag, Object date, Object valueOfCheckbox) {
    if (ObjectUtils.isEmpty(date)) {
      if (flag == DateTimeStatusEnum.JES.value || flag == DateTimeStatusEnum.BSC.value) {
        return "年　　月";
      } else {
        return "年　　月頃";
      }
    }
    return createJapaneseMonthYearFormat(flag, date, valueOfCheckbox);
  }

  public static String timestampFormatInPdf(LocalDateTime localDateTime) {
    if (ObjectUtils.isEmpty(localDateTime)) {
      return null;
    }

    DateTimeFormatter format = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT_IN_PDF);
    return localDateTime.format(format);
  }

  public static String dateFormatInFilename(LocalDateTime dateTime) {
    return dateFormatInFilename(dateTime.toLocalDate());
  }

  public static String dateFormatInFilename(LocalDate date) {
    if (ObjectUtils.isEmpty(date)) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_IN_FILENAME);
    return date.format(formatter);
  }

  public  Object japaneseDateFormat(Object date, Object valueOfCheckbox) {
    return this.createJapaneseDateFormatWithDefault(date, valueOfCheckbox, "年　 月 　日");
  }

  public  Object japaneseDateFormat(Object date, Object valueOfCheckbox, String defaultValue) {
    String defaultValue_5 = "ーーーーー年 ー月 ー日";
    String defaultValue_3 = "ーーー年 ー月 ー日";
    if (defaultValue.equals("df5")) {
      return this.createJapaneseDateFormatWithDefault(date, valueOfCheckbox, defaultValue_5);
    } else if (defaultValue.equals("df3")) {
      return this.createJapaneseDateFormatWithDefault(date, valueOfCheckbox, defaultValue_3);
    }
    return this.createJapaneseDateFormatWithDefault(date, valueOfCheckbox, "年　 月 　日");
  }

  public Object japaneseDateFormatWithEmptyDefault(Object date, Object valueOfCheckbox) {
    return this.createJapaneseDateFormatWithDefault(date, valueOfCheckbox, "");
  }

  public  Object createJapaneseDateFormatWithDefault(
      Object date, Object valueOfCheckbox, String defaultResult) {
    if (ObjectUtils.isEmpty(date)) {
      return defaultResult;
    }

    boolean isSeireki = !ObjectUtils.isEmpty(valueOfCheckbox) && valueOfCheckbox.equals(true);

    LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-M-d"));

    DateTimeFormatter format;
    if (isSeireki) {
      format = DateTimeFormatter.ofPattern(JAPANESE_DATE_FORMAT);
      return localDate.format(format).replaceAll("  0", "&nbsp;&nbsp;");
    } else {
      String result = DateUtil.getJpDateStringFromDateObj(localDate, JAPANESE_DATE_FORMAT_WITH_ERA);
      return result.replaceAll("  0", "&nbsp;&nbsp;");
    }
  }

  public Object japaneseDateFormatDateAndHour(Object date) {
    if (ObjectUtils.isEmpty(date)) {
      return null;
    }

    LocalDateTime localDateTime =
        LocalDateTime.parse(date.toString(), DateTimeFormatter.ISO_DATE_TIME);

    DateTimeFormatter format = DateTimeFormatter.ofPattern(JAPANESE_DATE_TIME_FORMAT);

    return localDateTime.format(format);
  }
}
