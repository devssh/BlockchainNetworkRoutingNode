package app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
    public static Gson gson = new GsonBuilder().create();


    public static <T> String ToJSON(T obj) {
        return gson.toJson(obj);
    }

    public static <T> T FromJSON(String json, Class<T> contractClass) {
        if(json.equals("[]")) {
            return gson.fromJson("", contractClass);
        }
        return gson.fromJson(json, contractClass);
    }
}
