package com.qartf.doseforreddit.mvp.data.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qartf.doseforreddit.mvp.data.model.Subreddit;

import java.lang.reflect.Type;

/**
 * Created by ART_F on 2017-09-05.
 */

public class SubredditDeserializer implements JsonDeserializer<Subreddit> {

    @Override
    public Subreddit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");

        Subreddit comment = new Subreddit(
                getNullAsEmptyString(jsonObject.get("kind")),
                getNullAsEmptyString(data.get("banner_img")),
                getNullAsEmptyString(data.get("user_is_banned")),
                getNullAsEmptyString(data.get("wiki_enabled")),
                getNullAsEmptyString(data.get("show_media")),
                getNullAsEmptyString(data.get("id")),
                getNullAsEmptyString(data.get("display_name_prefixed")),
                getNullAsEmptyString(data.get("submit_text")),
                getNullAsEmptyString(data.get("display_name")),
                getNullAsEmptyString(data.get("title")),
                getNullAsEmptyString(data.get("user_has_favorited")),
                getNullAsEmptyString(data.get("over18")),
                getNullAsEmptyString(data.get("description")),
                getNullAsEmptyString(data.get("user_is_muted")),
                getNullAsEmptyString(data.get("submit_link_label")),
                getNullAsEmptyString(data.get("subscribers")),
                getNullAsEmptyString(data.get("submit_text_label")),
                getNullAsEmptyString(data.get("whitelist_status")),
                getNullAsEmptyString(data.get("key_color")),
                getNullAsEmptyString(data.get("name")),
                getNullAsEmptyString(data.get("url")),
                getNullAsEmptyString(data.get("created_utc")),
                getNullAsEmptyString(data.get("user_is_contributor")),
                getNullAsEmptyString(data.get("subreddit_type")),
                getNullAsEmptyString(data.get("user_is_subscriber"))
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
