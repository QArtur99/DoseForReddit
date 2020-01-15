package com.qartf.doseforreddit.data.entity.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qartf.doseforreddit.data.entity.Subreddit;
import com.qartf.doseforreddit.utility.Utility;

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
                Utility.handleEscapeCharacter(getNullAsEmptyString(jsonObject.get("kind"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("banner_img"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("user_is_banned"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("wiki_enabled"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("show_media"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("id"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("display_name_prefixed"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("submit_text"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("display_name"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("title"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("user_has_favorited"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("over18"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("description"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("user_is_muted"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("submit_link_label"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("subscribers"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("submit_text_label"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("whitelist_status"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("key_color"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("name"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("url"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("created_utc"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("user_is_contributor"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("subreddit_type"))),
                Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("user_is_subscriber")))
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
