package com.wickedword.charlesr.sandwichclub.utils;

import android.util.Log;

import com.wickedword.charlesr.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private final static String TAG = JsonUtils.class.getSimpleName();

    private final static String NAME_KEY = "name";
    private final static String MAIN_NAME_KEY = "mainName";
    private final static String AKA_KEY = "alsoKnownAs";
    private final static String PLACE_OF_ORIGIN_KEY = "placeOfOrigin";
    private final static String DESCRIPTION_KEY = "description";
    private final static String IMAGE_KEY = "image";
    private final static String INGREDIENTS_KEY = "ingredients";

    public static Sandwich parseSandwichJson(String json) {
        // json requires a try/catch block
        try {
            // get the json, make some json objects and arrays to get the info
            JSONObject mainJsonObject = new JSONObject(json);
            JSONObject name = mainJsonObject.getJSONObject(NAME_KEY);
            String mainName = name.getString(MAIN_NAME_KEY);
            JSONArray JSONArrayAlsoKnownAs = name.getJSONArray(AKA_KEY);
            List<String> alsoKnownAs = jsonArrayToList(JSONArrayAlsoKnownAs);

            String placeOfOrigin = mainJsonObject.optString(PLACE_OF_ORIGIN_KEY);
            String description = mainJsonObject.getString(DESCRIPTION_KEY);
            String image = mainJsonObject.getString(IMAGE_KEY);

            JSONArray JSONArrayIngredients = mainJsonObject.getJSONArray(INGREDIENTS_KEY);
            List<String> ingredients = jsonArrayToList(JSONArrayIngredients);

            // send back the sandwich info
            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // makes an array into a list
    private static List<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }

        return list;
    }
}
