package com.lap.no21docssample.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class ObjectFormatUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String formatListToString(List<?> list, String type, Map<String, String[][]> tableData,String prefix, Map<String, String> mapAll) {
        StringBuilder sb = new StringBuilder();
         handleDataArraySpec(list, type,mapAll);



        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            try {
                map = mapper.convertValue(list.get(i), Map.class);
            } catch (Exception e) {
                try {
                    sb.append(mapper.writeValueAsString(list.get(i))
                            .replaceAll("[\\{\\}\"]", ""));
                } catch (JsonProcessingException ex) {
                    continue;
                }
                if (i < list.size() - 1) sb.append("\n");
                continue;
            }

            // Nếu có type thì xử lý switch-case
            if (type != null && !type.isEmpty()) {
                appendTableDataForType(type, tableData, map, prefix);
                appendObjectByType(type, sb, map);

            } else {
                // Không truyền type => flatten
                formatObject(map, sb);
            }

            if (i < list.size() - 1) sb.append("\n"); // Ngắt dòng giữa các phần tử
        }

        return sb.toString();
    }



    private static void appendTableDataForType(String type, Map<String, String[][]> tableData, Map<String, Object> map,String prefix) {
        String[][] newData;
        switch (type) {
            case "kainushishomei":
                newData = new String[][]{
                        {
                                !map.getOrDefault("kainushi_type","").equals("その他フリーワード") ? safe(map.get("kainushi_type")) : safe(map.get("sonohoka_type")),
                                "住所　" + safe(map.get("jusho")),
                                ""
                        },
                        {
                                "",
                                "氏名　" + safe(map.get("shimei")),
                                safe(map.get("takkenshijoho_torokubango")) + "\n" + safe(map.get("takkenshijoho_shimei")),
                        }

                };
                break;
            case "urinushishomei":
                newData = new String[][]{
                        {
                                !map.getOrDefault("urinushi_type","").equals("その他フリーワード") ? safe(map.get("urinushi_type")) : safe(map.get("sonohoka_type")),
                                "住所　" + safe(map.get("jusho")),
                                ""
                        },
                        {
                                "",
                                "氏名　" + safe(map.get("shimei")),
                                safe(map.get("takkenshijoho_torokubango")) + "\n" + safe(map.get("takkenshijoho_shimei")),
                        }

                };
                break;
            default:
               return;
        }
        if (tableData.containsKey(prefix)) {
            tableData.put(prefix, mergeTableData(tableData.get(prefix), newData));
        } else {
            tableData.put(prefix, newData);
        }
    }


    private static boolean isEmpty(Object val) {
        return val == null || val.toString().trim().isEmpty();
    }

    private static String safe(Object val) {
        return val == null ? "" : val.toString();
    }

    private static void formatObject(Map<String, Object> map, StringBuilder sb) {
        List<String> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String value = entry.getValue() != null ? entry.getValue().toString() : ConstantUtils.STRING_NOT_FOUND;
            values.add(value);
        }
        sb.append(String.join("| ", values));
    }

    /**
     * Phương thức này sẽ định dạng các trường của đối tượng theo từng loại khác nhau.
     * Nếu không có loại cụ thể, nó sẽ flatten toàn bộ đối tượng.
     *
     * @param type Loại của đối tượng (ví dụ: "tochi", "shikiken_mokutekitochi", v.v.)
     * @param sb   StringBuilder để append kết quả
     * @param map  Đối tượng cần định dạng
     */

    public static void appendObjectByType(String type, StringBuilder sb, Map<String, Object> map) {
        switch (type) {
            case "tochi":
                sb.append(safe(map.get("shozai"))).append("｜")
                        .append(safe(map.get("chibanmae"))).append("番").append(safe(map.get("chibanato"))).append("｜")
                        .append(!isEmpty(map.get("chimoku1")) ? safe(map.get("chimoku1")) : safe(map.get("chimoku2"))).append("｜")
                        .append(safe(map.get("chiseki"))).append("｜")
                        .append(safe(map.get("mochibun1"))).append("/").append(safe(map.get("mochibun2")));
                break;

            case "shikiken_mokutekitochi":
                sb.append(safe(map.get("shozai"))).append("｜")
                        .append(safe(map.get("chibanmae"))).append("番").append(safe(map.get("chibanato"))).append("｜")
                        .append(!isEmpty(map.get("chimoku1")) ? safe(map.get("chimoku1")) : safe(map.get("chimoku2"))).append("｜")
                        .append(safe(map.get("chiseki"))).append("｜")
                        .append(safe(map.get("shikichiken_wariai1"))).append("/").append(safe(map.get("shikichiken_wariai2")));
                break;

            case "teiki_shakuchiken_tochi":
                sb.append(safe(map.get("shozai"))).append("｜")
                        .append(safe(map.get("chibanmae"))).append("番").append(safe(map.get("chibanato"))).append("｜")
                        .append(!isEmpty(map.get("chimoku1")) ? safe(map.get("chimoku1")) : safe(map.get("chimoku2"))).append("｜")
                        .append(safe(map.get("chiseki")));
                break;

            case "shakuchiken_tochi":
                sb.append(safe(map.get("shozai"))).append("｜")
                        .append(safe(map.get("chibanmae"))).append("番").append(safe(map.get("chibanato"))).append("｜")
                        .append(!isEmpty(map.get("chimoku1")) ? safe(map.get("chimoku1")) : safe(map.get("chimoku2"))).append("｜")
                        .append(safe(map.get("chiseki")));
                break;

            default:
                // Không match type => flatten
                formatObject(map, sb);
                break;
        }
    }

    private static void handleDataArraySpec(List<?> list, String type, Map<String, String> mapAll) {

        switch (type) {
            case "tochi":
                mapAll.put( "M007-02_ARRAY_KEY1",
                        groupByDataArraySpecGroupBy(list,
                                "shozai",
                                map -> {
                            StringBuilder stringBuilder = new StringBuilder();
                            if (map.get("chibanmae") != null && !map.get("chibanmae").toString().isEmpty()) {
                                stringBuilder.append(safe(map.get("chibanmae"))).append("番");
                            }
                            if (map.get("chibanato") != null && !map.get("chibanato").toString().isEmpty()) {
                                stringBuilder.append(safe(map.get("chibanato")));
                            }
                            return stringBuilder.toString();
                        }));

                mapAll.put( "M007-02_ARRAY_KEY2", groupByDataArraySpec(list,  map -> safe(map.get("chibanmae")) + "番" + safe(map.get("chibanato"))));
                return ;

            // Add more cases as needed
            default:

        }


    }

    private static String groupByDataArraySpecGroupBy(List<?> list, String keyGroupBy, Function<Map, String> handleDataArraySpec) {
        StringBuilder sb = new StringBuilder();
        list.stream()
                .collect(Collectors.groupingBy(o -> {
                    Map<String, Object> map = mapper.convertValue(o, Map.class);
                    return safe(map.get(keyGroupBy));
                }))
                .forEach((key, value) -> {
                    sb.append(key);

                    String joined = value.stream()
                            .map(item -> {
                                Map<String, Object> map = mapper.convertValue(item, Map.class);
                                return handleDataArraySpec.apply(map);
                            })
                            .collect(Collectors.joining("、")); // ngăn cách bằng dấu phẩy
                    if (key != null && !key.isEmpty() && !joined.isEmpty()){
                      sb.append(" ");
                    }
                    sb.append(joined).append("\n");
                });
        return sb.toString();
    }

    private static String groupByDataArraySpec(List<?> list, Function<Map, String> handleDataArraySpec) {
        return list.stream().map(value -> {
            Map<String, Object> map = mapper.convertValue(value, Map.class);
            return handleDataArraySpec.apply(map);
        }).collect(Collectors.joining("、"));
    }



    private static String[][] mergeTableData(String[][] oldData, String[][] newData) {
        int rows = oldData.length + newData.length;
        int cols = Math.max(oldData[0].length, newData[0].length);
        String[][] merged = new String[rows][cols];

        // Copy old
        for (int i = 0; i < oldData.length; i++) {
            System.arraycopy(oldData[i], 0, merged[i], 0, oldData[i].length);
        }
        // Copy new
        for (int i = 0; i < newData.length; i++) {
            System.arraycopy(newData[i], 0, merged[oldData.length + i], 0, newData[i].length);
        }

        return merged;
    }

}
