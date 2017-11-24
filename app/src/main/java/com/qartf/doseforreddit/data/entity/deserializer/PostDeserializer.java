package com.qartf.doseforreddit.data.entity.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.utility.Utility;

import java.lang.reflect.Type;

public class PostDeserializer implements JsonDeserializer<Post> {
    Post date;

    @Override
    public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has("postHint")) {
            date = new Post(
                    jsonObject.get("kind").getAsString(),
                    jsonObject.get("domain").getAsString(),
                    jsonObject.get("subreddit").getAsString(),
                    jsonObject.get("selftext").getAsString(),
                    jsonObject.get("likes").getAsString(),
                    jsonObject.get("linkFlairText").getAsString(),
                    jsonObject.get("id").getAsString(),
                    jsonObject.get("title").getAsString(),
                    jsonObject.get("saved").getAsString(),
                    jsonObject.get("previewUrl").getAsString(),
                    jsonObject.get("previewGif").getAsString(),
                    jsonObject.get("thumbnail").getAsString(),
                    jsonObject.get("subRedditId").getAsString(),
                    jsonObject.get("postHint").getAsString(),
                    jsonObject.get("parentWhiteListStatus").getAsString(),
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("permaLink").getAsString(),
                    jsonObject.get("subRedditType").getAsString(),
                    jsonObject.get("url").getAsString(),
                    jsonObject.get("whiteListStatus").getAsString(),
                    jsonObject.get("author").getAsString(),
                    jsonObject.get("createdUTC").getAsString(),
                    jsonObject.get("subredditNamePrefixed").getAsString(),
                    jsonObject.get("ups").getAsString(),
                    jsonObject.get("numComents").getAsString()
            );
        } else {
            JsonObject data = jsonObject.getAsJsonObject("data");
            JsonObject preview = checkImages(data);

            date = new Post(
                    Utility.handleEscapeCharacter(getNullAsEmptyString(jsonObject.get("kind"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("domain"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("subreddit"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("selftext"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("likes"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("link_flair_text"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("id"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("title"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("saved"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(getImageSource(preview))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(getMp4(preview))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("thumbnail"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("subreddit_id"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("post_hint"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("parent_whitelist_status"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("name"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("permalink"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("subreddit_type"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("url"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("whitelist_status"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("author"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("created_utc"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("subreddit_name_prefixed"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("ups"))),
                    Utility.handleEscapeCharacter(getNullAsEmptyString(data.get("num_comments")))
            );
        }
        return date;
    }

    private JsonObject checkImages(JsonObject preview) {
        JsonObject jsonObject = null;
        if (preview.has("preview")) {
            jsonObject = preview.getAsJsonObject("preview");
            if (jsonObject.has("images")) {
                jsonObject = jsonObject.getAsJsonArray("images").get(0).getAsJsonObject();
            }
        }
        return jsonObject;
    }

    private JsonElement getImageSource(JsonObject preview) {
        JsonElement previewUrl = null;
        if (preview != null && preview.has("source")) {
            previewUrl = preview.getAsJsonObject("source").get("url");
        }
        return previewUrl;
    }

    private JsonElement getMp4(JsonObject preview) {
        JsonElement previewMp4 = null;
        if (preview != null && preview.has("variants")) {
            JsonObject variants = preview.getAsJsonObject("variants");
            if (variants.has("mp4")) {
                previewMp4 = variants.getAsJsonObject("mp4").getAsJsonObject("source").get("url");
            }
        }
        return previewMp4;
    }

    private String getNullAsEmptyString(JsonElement jsonElement) {
        if (jsonElement == null) {
            return "";
        }
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }

}
