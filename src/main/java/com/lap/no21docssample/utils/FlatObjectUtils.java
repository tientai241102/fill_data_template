package com.lap.no21docssample.utils;

import java.util.List;
import java.util.Map;

import static com.lap.no21docssample.utils.DateUtils.convertToJapaneseFormatIfDate;

public class FlatObjectUtils {


    @SuppressWarnings("unchecked")
    public static void flattenObjectToKeyValueMap(String prefix, Object obj, Map<String, String> map, Map<String, String> objectWithDataType, Map<String, String> radioButtonData, Map<String, String> objectWithLabel,  Map<String, String[][]> tableData ) {
        String type = prefix.contains(".") ? prefix.substring(prefix.lastIndexOf('.') + 1) : prefix;

        if (obj instanceof Map) {
            StringBuilder sb = new StringBuilder();

            ObjectFormatUtils.appendObjectByType(type, sb, (Map<String, Object>) obj);
            if (!prefix.isEmpty()) {
                map.put(prefix, sb.toString());
            }
            ((Map<String, Object>) obj).forEach((k, v) -> {
                String key = prefix.isEmpty() ? k : prefix + "." + k;
                flattenObjectToKeyValueMap(key, v, map, objectWithDataType, radioButtonData, objectWithLabel,tableData);
            });
        } else if (obj instanceof List || obj instanceof Object[]) {
            List<?> list = (List<?>) obj;
            map.put(prefix, ObjectFormatUtils.formatListToString(list, type,tableData,prefix));
            for (int i = 0; i < list.size(); i++) {
                flattenObjectToKeyValueMap(prefix + "[" + i + "]", list.get(i), map, objectWithDataType, radioButtonData, objectWithLabel,tableData);
            }
        } else {
            String text = obj != null ? obj.toString() : null;

            map.put(prefix, wrapTextWithTypeHandling(prefix, text, objectWithDataType, radioButtonData));
        }

    }

    private static String wrapText(String text, int maxLen) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (char c : text.toCharArray()) {
            sb.append(c);
            count++;
            if (c == '\n' || count >= maxLen) {
                sb.append('\n');
                count = 0;
            }
        }
        return sb.toString();
    }

    private static String wrapTextWithTypeHandling(String key, String value, Map<String, String> objectWithDataType, Map<String, String> radioButtonData) {
        // Todo handle Label
        return wrapText(resolveValueByType(key, value, objectWithDataType, radioButtonData), 50); // Wrap text if not a checkbox

    }


    private static String resolveValueByType(String key, String value, Map<String, String> objectWithDataType, Map<String, String> radioButtonData) {
        String extractedKey = key.contains(".") ? key.substring(key.lastIndexOf('.') + 1) : key;
        switch (objectWithDataType.getOrDefault(extractedKey, "")) {
            case ConstantUtils.DATE:
                if (value == null || value.isEmpty()) {
                    return ConstantUtils.STRING_NOT_FOUND;
                } else {
                    return convertToJapaneseFormatIfDate(value);
                }

            case ConstantUtils.CHECKBOX:
                if (value == null || value.isEmpty()) {
                    return ConstantUtils.STRING_UNCHECKED;

                } else {
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1")) {
                        return ConstantUtils.STRING_CHECKED;
                    } else {
                        return ConstantUtils.STRING_UNCHECKED;
                    }
                }
            case ConstantUtils.RADIOBUTTON:
                radioButtonData.put(key, value);
                return value;

            default:
                if (value == null || value.isEmpty()) {
                    return ConstantUtils.STRING_NOT_FOUND;

                } else {
                    return value;
                }
        }
    }


}
