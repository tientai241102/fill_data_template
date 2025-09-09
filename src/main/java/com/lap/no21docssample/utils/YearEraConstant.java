package com.lap.no21docssample.utils;

import java.util.LinkedHashMap;

public interface YearEraConstant {

  /**
   * get constant
   *
   * @return
   */
  static LinkedHashMap<String, Object> getValues() {
    LinkedHashMap<String, Object> obj = new LinkedHashMap<>();
    obj.put("JP_YEAR", getJpYear());
    obj.put("YEARS", getYears());
    return obj;
  }

  /**
   * JP Year
   *
   * @return array
   */
  static LinkedHashMap<String, Object> getJpYear() {
    LinkedHashMap<String, Object> jpYear = new LinkedHashMap<>();
    jpYear.put("MEIJI", "明治");
    jpYear.put("TAISHO", "大正");
    jpYear.put("SHOWA", "昭和");
    jpYear.put("HEISEI", "平成");
    jpYear.put("AD_2019", "令和");

    return jpYear;
  }

  /**
   * List EraYear
   *
   * @return array
   */
  static LinkedHashMap<String, Object> getYears() {
    LinkedHashMap<String, Object> years = new LinkedHashMap<>();
    LinkedHashMap<String, Object> item;

    // 1868;
    item = new LinkedHashMap<>();
    item.put("ID", "MEIJI");
    item.put("FROM", "1868-01-25");
    item.put("TO", "1912-07-29");
    item.put("JP", "明治");
    years.put("1868", item);

    // 1912
    item = new LinkedHashMap<>();
    item.put("ID", "TAISHO");
    item.put("FROM", "1912-07-30");
    item.put("TO", "1926-12-24");
    item.put("JP", "大正");
    years.put("1912", item);

    // 1926
    item = new LinkedHashMap<>();
    item.put("ID", "SHOWA");
    item.put("FROM", "1926-12-25");
    item.put("TO", "1989-01-07");
    item.put("JP", "昭和");
    years.put("1926", item);

    // 1989
    item = new LinkedHashMap<>();
    item.put("ID", "HEISEI");
    item.put("FROM", "1989-01-08");
    item.put("TO", "2019-04-30");
    item.put("JP", "平成");
    years.put("1989", item);

    // 2019
    item = new LinkedHashMap<>();
    item.put("ID", "AD_2019");
    item.put("FROM", "2019-05-01");
    item.put("TO", null);
    item.put("JP", "令和");
    years.put("2019", item);

    return years;
  }
}
