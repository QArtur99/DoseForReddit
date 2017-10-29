package com.qartf.doseforreddit.mvp.data.entity.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.mvp.data.entity.Subreddit;
import com.qartf.doseforreddit.mvp.data.entity.SubredditParent;

import java.lang.reflect.Type;
import java.util.List;

public class SubredditParentDeserializer implements JsonDeserializer<SubredditParent> {

    @Override
    public SubredditParent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonArray array = data.getAsJsonArray("children");
        List<Subreddit> subredditList = new Gson().fromJson(array.toString(), new TypeToken<List<Subreddit>>() {}.getType());

        SubredditParent subredditParent = new SubredditParent(
                subredditList,
                getNullAsEmptyString(data.get("after")),
                getNullAsEmptyString(data.get("before"))
        );

        return subredditParent;
    }

    private String getNullAsEmptyString(JsonElement jsonElement) {
        if (jsonElement == null) {
            return "";
        }
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
}
