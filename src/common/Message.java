package common;

import org.json.JSONObject;

public class Message {
    private String username;
    private Boolean handshake;
    private String body;

    public Message(String username, String body) {
        this.body = body;
        this.username = username;
        this.handshake = false;
    }

    public Message(String username, String body, Boolean handshake) {
        this.body = body;
        this.username = username;
        this.handshake = handshake;
    }

    public String getBody() {
        return body;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", this.username);
        json.put("body", this.body);
        json.put("handshake", this.handshake);

        return json;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getHandshake() {
        return handshake;
    }

    public void setHandshake(Boolean handshake) {
        this.handshake = handshake;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static Message fromJson(JSONObject json) {
        return new Message(
            json.getString("username"),
            json.getString("body"),
            json.getBoolean("handshake")
        );
    }
}
