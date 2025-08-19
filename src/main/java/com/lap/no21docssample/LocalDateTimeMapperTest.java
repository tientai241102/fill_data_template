package com.lap.no21docssample;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalDateTimeMapperTest {

    // Class sample
    static class KeiyakuView {
        private String name;
        private LocalDateTime createdAt;

        public KeiyakuView(String name, LocalDateTime createdAt) {
            this.name = name;
            this.createdAt = createdAt;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> tatakkengyoshajoho = new HashMap<>();
        tatakkengyoshajoho.put("menkyo_nengappi", LocalDate.of(2025, 8, 15));
        KeiyakuView sample = new KeiyakuView("Test Contract", LocalDateTime.now());
        List<Object> arrayList = new ArrayList<>();
        arrayList.add(tatakkengyoshajoho);

        Map<String, Object> bsc = new HashMap<>();
        bsc.put("tatakkengyoshajoho", arrayList);

        Map<String, Object> baikei = new HashMap<>();
        baikei.put("bsc", bsc);

        Map<String, Object> keiyakuView = new HashMap<>();
        keiyakuView.put("baikei", baikei);

        // Mapper không cấu hình → sẽ lỗi
        try {
            ObjectMapper defaultMapper = new ObjectMapper();
            System.out.println(defaultMapper.writeValueAsString(keiyakuView));
        } catch (Exception e) {
            System.out.println("❌ Error (no config): " + e.getMessage());
        }

        // 2️⃣ Có cấu hình mapper custom LocalDateTime
        ObjectMapper mapperCustom = createMapper();

        System.out.println("✅ With config: " + mapperCustom.convertValue(keiyakuView, Map.class));
        System.out.println(mapperCustom.convertValue(sample,Map.class));
    }


    public static ObjectMapper createMapper() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }



//    public static void main(String[] args) throws JsonProcessingException {
//        KeiyakuView sample = new KeiyakuView("Test Contract", LocalDateTime.now());
//
//        // 1️⃣ Không cấu hình mapper
//        ObjectMapper mapperDefault = new ObjectMapper();
//        try {
//            System.out.println("No config:");
//            System.out.println(mapperDefault.writeValueAsString(sample)); // ❌ sẽ lỗi
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//
//        // 2️⃣ Có cấu hình mapper custom LocalDateTime
//        ObjectMapper mapperCustom = new ObjectMapper();
//
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
//            @Override
//            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
//                    throws IOException {
//                gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            }
//        });
//
//        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
//            @Override
//            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
//                    throws IOException {
//                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            }
//        });
//
//        mapperCustom.registerModule(module);
//
//        System.out.println("\nWith config:");
//        System.out.println(mapperCustom.writeValueAsString(sample)); // ✅ OK
//    }
}
