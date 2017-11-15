package com.qartf.doseforreddit.data.entity.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.data.entity.Comment;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ART_F on 2017-09-03.
 */

public class CommentDeserializer implements JsonDeserializer<Comment> {

    @Override
    public Comment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");


        if (jsonObject.get("kind").getAsString().equals("more")) {

            List<String> commentList = null;
            if (data.get("children").isJsonArray()) {
                JsonArray array = data.getAsJsonArray("children");
                commentList = new Gson().fromJson(array.toString(), new TypeToken<List<String>>() {}.getType());
            }

            Comment comment = new Comment(

                    getNullAsEmptyString(jsonObject.get("kind")),
                    getNullAsEmptyString(data.get("parent_id")),
                    getNullAsEmptyString(data.get("name")),
                    commentList
            );
            return comment;
        }


        List<Comment> commentList = null;
        if (data.get("replies").isJsonObject()) {
            JsonObject replies = data.getAsJsonObject("replies");
            JsonObject datax = replies.getAsJsonObject("data");
            JsonArray array = datax.getAsJsonArray("children");
            commentList = new Gson().fromJson(array.toString(), new TypeToken<List<Comment>>() {}.getType());
        }

        Comment comment = new Comment(
                getNullAsEmptyString(jsonObject.get("kind")),
                getNullAsEmptyString(data.get("subreddit_id")),
                getNullAsEmptyString(data.get("link_id")),
                commentList,
                getNullAsEmptyString(data.get("saved")),
                getNullAsEmptyString(data.get("author")),
                getNullAsEmptyString(data.get("ups")),
                getNullAsEmptyString(data.get("parent_id")),
                getNullAsEmptyString(data.get("score")),
                getNullAsEmptyString(data.get("body")),
                getNullAsEmptyString(data.get("subreddit_type")),
                getNullAsEmptyString(data.get("name")),
                getNullAsEmptyString(data.get("depth")),
                getNullAsEmptyString(data.get("created_utc")),
                getNullAsEmptyString(data.get("subreddit_name_prefixed"))

        );

        return comment;
    }

    private String getNullAsEmptyString(JsonElement jsonElement) {
        if (jsonElement == null) {
            return "";
        }
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
}
