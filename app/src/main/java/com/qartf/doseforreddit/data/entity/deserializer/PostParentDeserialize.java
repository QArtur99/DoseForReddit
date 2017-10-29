package com.qartf.doseforreddit.data.entity.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.entity.Post;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ART_F on 2017-09-20.
 */

public class PostParentDeserialize implements JsonDeserializer<PostParent> {

    @Override
    public PostParent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonArray array = data.getAsJsonArray("children");
        List<Post> commentList = new Gson().fromJson(array.toString(), new TypeToken<List<Post>>() {}.getType());


        PostParent postParent = new PostParent(
                commentList,
                getNullAsEmptyString(data.get("after")),
                getNullAsEmptyString(data.get("before"))
        );


        return postParent;
    }

    private String getNullAsEmptyString(JsonElement jsonElement) {
        if (jsonElement == null) {
            return "";
        }
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }

}
