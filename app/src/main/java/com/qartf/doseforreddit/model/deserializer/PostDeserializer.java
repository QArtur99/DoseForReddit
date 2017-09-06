package com.qartf.doseforreddit.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qartf.doseforreddit.model.PostObject;

import java.lang.reflect.Type;

public class PostDeserializer implements JsonDeserializer<PostObject> {
    PostObject date;

    @Override
    public PostObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has("postHint")) {
            date = new PostObject(
                    jsonObject.get("kind").getAsString(),
                    jsonObject.get("domain").getAsString(),
                    jsonObject.get("subreddit").getAsString(),
                    jsonObject.get("selftext").getAsString(),
                    jsonObject.get("linkFlairText").getAsString(),
                    jsonObject.get("id").getAsString(),
                    jsonObject.get("title").getAsString(),
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

            date = new PostObject(
                    getNullAsEmptyString(jsonObject.get("kind")),
                    getNullAsEmptyString(data.get("domain")),
                    getNullAsEmptyString(data.get("subreddit")),
                    getNullAsEmptyString(data.get("selftext")),
                    getNullAsEmptyString(data.get("link_flair_text")),
                    getNullAsEmptyString(data.get("id")),
                    getNullAsEmptyString(data.get("title")),
                    getNullAsEmptyString(getImageSource(preview)),
                    getNullAsEmptyString(getMp4(preview)),
                    getNullAsEmptyString(data.get("thumbnail")),
                    getNullAsEmptyString(data.get("subreddit_id")),
                    getNullAsEmptyString(data.get("post_hint")),
                    getNullAsEmptyString(data.get("parent_whitelist_status")),
                    getNullAsEmptyString(data.get("name")),
                    getNullAsEmptyString(data.get("permalink")),
                    getNullAsEmptyString(data.get("subreddit_type")),
                    getNullAsEmptyString(data.get("url")),
                    getNullAsEmptyString(data.get("whitelist_status")),
                    getNullAsEmptyString(data.get("author")),
                    getNullAsEmptyString(data.get("created_utc")),
                    getNullAsEmptyString(data.get("subreddit_name_prefixed")),
                    getNullAsEmptyString(data.get("ups")),
                    getNullAsEmptyString(data.get("num_comments"))
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
