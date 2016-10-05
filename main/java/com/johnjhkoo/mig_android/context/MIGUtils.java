package com.johnjhkoo.mig_android.context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JohnKoo on 6/24/16.
 */
public class MIGUtils {

    private static final String TAG = "MIGUtils";

    private static final Gson gson = new GsonBuilder().create();
    private static final JsonParser parser = new JsonParser();

    public static Gson getGson() {
        return gson;
    }

    public static JsonParser getParser() {
        return parser;
    }

    public static List<MIGEventVO> migJsonArrayToApiDataVOList(JsonArray arr) {
        List<MIGEventVO> list = new ArrayList<>();

        for (JsonElement e : arr) {
            MIGEventVO vo = gson.fromJson(e, MIGEventVO.class);
            list.add(vo);
        }

        return list;
    }

    public static List<MIGEventVO> fromJsonObjectToVOList(JsonObject object) {
        JsonArray arr = object.get("data").getAsJsonArray();

        return gson.fromJson(arr, new TypeToken<List<MIGEventVO>>(){}.getType());
    }

    public static int fromJsonObjectToItemsCount(JsonObject object) {
        return Integer.valueOf(object.get("itemsCount").getAsString());
    }
}
