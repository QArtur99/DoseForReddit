package com.qartf.doseforreddit.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TestDeserializer implements JsonDeserializer<TestObject> {

    @Override
    public TestObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonObject preview = checkImages(data);

        TestObject date = new TestObject(
                jsonObject.get("kind").toString(),
                data.get("domain").toString(),
                data.get("subreddit").toString(),
                data.get("selftext").toString(),
                data.get("link_flair_text").toString(),
                data.get("id").toString(),
                data.get("title").toString(),
                getImageSource(preview),
                getMp4(preview),
                data.get("thumbnail").toString(),
                data.get("subreddit_id").toString(),
                data.get("post_hint").toString(),
                data.get("parent_whitelist_status").toString(),
                data.get("name").toString(),
                data.get("permalink").toString(),
                data.get("subreddit_type").toString(),
                data.get("url").toString(),
                data.get("whitelist_status").toString(),
                data.get("author").toString(),
                data.get("created_utc").toString(),
                data.get("subreddit_name_prefixed").toString(),
                data.get("ups").toString(),
                data.get("num_comments").toString()
        );

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

    private String getImageSource(JsonObject preview) {
        String previewUrl = "";
        if (preview.has("source")) {
            previewUrl = preview.getAsJsonObject("source").get("url").toString();
        }
        return previewUrl;
    }

    private String getMp4(JsonObject preview) {
        String previewMp4 = "";
        if (preview.has("variants")) {
            JsonObject variants = preview.getAsJsonObject("variants");
            if (variants.has("mp4")) {
                previewMp4 = variants.getAsJsonObject("mp4").getAsJsonObject("source").get("url").toString();
            }
        }
        return previewMp4;
    }

}
