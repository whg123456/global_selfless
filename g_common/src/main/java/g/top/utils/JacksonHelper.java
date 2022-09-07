package g.top.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * JSON工具类
 */

@Slf4j
public class JacksonHelper {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(Date.class, new CustomDateDeserializer());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new ParameterNamesModule())
                .registerModule(javaTimeModule);
        ;

        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    private JacksonHelper() {
    }

    @SneakyThrows
    public static String serialize(Object o) {
        return mapper.writeValueAsString(o);
    }

    @SneakyThrows
    public static String serialize(Object o, boolean pretty) {
        if (pretty) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        }
        return mapper.writeValueAsString(o);
    }


    @SneakyThrows
    public static <T> T deserialize(String s, Class<T> clazz) {
        if (s == null || s.length() == 0)
            return null;


        T t = mapper.readValue(s, clazz);
        return t;

    }

    @SneakyThrows
    public static <T> T deserialize(String s, JavaType javaType) {
        if (s == null || s.length() == 0)
            return null;

        T t = mapper.readValue(s, javaType);
        return t;
    }

    @SneakyThrows
    public static <T> T deserialize(String s, TypeReference<T> clazz) {

        if (s == null || s.length() == 0)
            return null;

        T t = (T) mapper.readValue(s, clazz);
        return t;
    }

    public static <T> T deserialize(String s, Class<T> parametrized, Class<?>... parameterClasses) {
        if (s == null || s.length() == 0)
            return null;

        try {
            if (parameterClasses == null || parameterClasses.length == 0) {
                T t = mapper.readValue(s, parametrized);
            }
            T t = mapper.readValue(s, TypeFactory.defaultInstance().constructParametricType(parametrized, parameterClasses));
            return t;
        } catch (IOException e) {
            log.error(s + ", " + e.getMessage());
        }
        return null;
    }

    @SneakyThrows
    public static JsonNode deserialize(String s) {
        if (s == null || s.length() == 0)
            return null;

        JsonNode t = mapper.readTree(s);
        return t;
    }

    public static TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }

    static class CustomDateDeserializer extends DateDeserializers.DateDeserializer {


        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

            Date date = null;

            try {
                date = super.deserialize(p, ctxt);
            } catch (IOException e) {
            }


            if (date == null) {
                date = DateHelper.deserialize(p.getText());
            }

            return date;
        }
    }


}
