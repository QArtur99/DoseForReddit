package com.qartf.doseforreddit.data.entity.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qartf.doseforreddit.data.entity.SubmitParent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubmitParentDeserializer implements JsonDeserializer<SubmitParent> {

    @Override
    public SubmitParent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("jquery");


        List<List<String>> resultList = new ArrayList<>();
        List<JsonArray> firstList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            firstList.add(jsonArray.get(i).getAsJsonArray());
        }


        for (JsonArray secondStringList : firstList) {
            List<String> secondList = new ArrayList<>();
            for (int i = 0; i < secondStringList.size(); i++) {
                String jsonElementString = secondStringList.get(i).toString();
                String stringValue = jsonElementString.replace("[", "")
                        .replace("]", "").replace("\"", "");
                secondList.add(stringValue);
            }
            resultList.add(secondList);
        }


        SubmitParent commentParent = new SubmitParent(
                jsonObject.get("success").getAsBoolean(),
                resultList
        );

        return commentParent;
    }

    private String getNullAsEmptyString(JsonElement jsonElement) {
        if (jsonElement == null) {
            return "";
        }
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
}
