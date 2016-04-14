package com.luisburgos.gpsbeaconnfc.util.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by luisburgos on 6/04/16.
 */
public class JSONHelper {
    public static String prettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement element = parser.parse(jsonString);
        return gson.toJson(element);
    }
}
