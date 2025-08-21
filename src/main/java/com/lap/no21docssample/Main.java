package com.lap.no21docssample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

        public static void main(String[] args) {
            String input = "{{M007-02_KEY1}}{{M007-02_KEY2}}";
            Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
            Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {
                System.out.println(matcher.group(1));
            }
        }
}
