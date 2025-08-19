package com.lap.no21docssample;

public class Main {
    public static void main(String[] args) {
        String key = "label:tatakkengyoshajoho.menkyo_nengappi";
        String test =
                null;
        System.out.println(test.trim());
        System.out.println( key.contains(":") ? key.substring(key.lastIndexOf(':') + 1) : key);
    }
}
