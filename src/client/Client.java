package client;

import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static final String HOST = "127.0.0.1";
    public static final Integer PORT = 8000;

    public static void main(String[] args) throws IOException {
        DisplayOutput displayOutput = new TerminalDisplayOutput();

        Socket serverSocket = new Socket(HOST, PORT);

        ServerConnection serverConnection = new ServerConnection(serverSocket, displayOutput);

        JSONObject json = new JSONObject();
        json.put("hello", "world!");

        serverConnection.sendMessage(json.toString());
    }
}
