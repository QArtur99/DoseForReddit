package com.qartf.doseforreddit.model.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.model.Comment;
import com.qartf.doseforreddit.model.CommentParent;

import java.lang.reflect.Type;
import java.util.List;

public class CommentParentDeserializer  implements JsonDeserializer<CommentParent> {

    @Override
    public CommentParent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray().get(1).getAsJsonObject().getAsJsonObject("data").getAsJsonArray("children");
        List<Comment> commentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Comment>>() {}.getType());

        CommentParent commentParent = new CommentParent(
                commentList
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