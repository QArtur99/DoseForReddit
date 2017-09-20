package com.qartf.doseforreddit.model.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.model.PostObjectParent;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ART_F on 2017-09-20.
 */

public class PostObjectParentDeserialize implements JsonDeserializer<PostObjectParent> {

    @Override
    public PostObjectParent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        List<PostObject> commentList = null;
        JsonArray array = data.getAsJsonArray("children");
        commentList = new Gson().fromJson(array.toString(), new TypeToken<List<PostObject>>() {}.getType());


        PostObjectParent postObjectParent = new PostObjectParent(
                commentList,
                getNullAsEmptyString(data.get("after")),
                getNullAsEmptyString(data.get("before"))
        );


        return postObjectParent;
    }

    private String getNullAsEmptyString(JsonElement jsonElement) {
        if (jsonElement == null) {
            return "";
        }
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }

}
