package com.hofftech.deliverysystem.util;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * Класс для работы с json
 */
public class JsonUtil {
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    /**
     * Десериализует json в объект
     *
     * @param json     строка
     * @param classOfT тип целевого объекта
     * @return целевой объект
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * Сериализует объект в json
     * @param src объект, который надо сериализовать
     * @return json строка
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }
}
