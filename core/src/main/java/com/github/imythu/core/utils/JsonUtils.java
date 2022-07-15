package com.github.imythu.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author imythu
 */
public class JsonUtils {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().create();
    }

    private JsonUtils() {}

    public static <T> T toObj(String jsonStr) {
        return GSON.fromJson(jsonStr, new TypeToken<T>() {}.getType());
    }

    public static <T> String toJson(T obj) {
        return GSON.toJson(obj);
    }
}
