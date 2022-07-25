package common;

import org.json.JSONObject;

public class Message {
    private String body;

    public Message(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("body", this.body);

        return json;
    }

    public static Message fromJson(JSONObject json) {
        return new Message(json.getString("body"));
    }
}
