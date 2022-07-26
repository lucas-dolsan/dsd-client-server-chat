package client;

import common.Message;
import common.SocketReader;
import common.SocketWriter;
import org.json.JSONObject;

import java.net.Socket;

public class ServerConnection extends Thread {
    private Socket serverSocket;
    private SocketReader reader;
    private SocketWriter writer;
    private ChatDisplay chatDisplay;

    public ServerConnection(Socket serverSocket, ChatDisplay chatDisplay) {
        if(chatDisplay == null) {
            throw new RuntimeException("Error: DisplayOutput not properly configured");
        }
        this.chatDisplay = chatDisplay;
        this.serverSocket = serverSocket;

        this.reader = new SocketReader(this.serverSocket);
        this.writer = new SocketWriter(this.serverSocket, false);

        this.connect();

        this.chatDisplay.setServerConnection(this);
    }

    public void sendMessage(Message message) {
        System.out.println("sendMessage: " + message.getBody());

        writer.write(message.toJson().toString());
    }

    public void connect() {
        Message message = new Message(chatDisplay.getUsername(), "", true);
        System.out.println("handshake: " + message.getUsername());

        writer.write(message.toJson().toString());
    }

    private void log(Message message) {
        System.out.println(" got: " + message.getBody());
    }

    @Override
    public void run() {
        String rawMessage;
        while (true) {
            rawMessage = this.reader.read();
            System.out.println("raw: " + rawMessage);
            if(!SocketReader.isMessageValid(rawMessage)) {
                break;
            }

            Message message = Message.fromJson(new JSONObject(rawMessage));
            this.log(message);
            this.chatDisplay.print(message);
        }
        this.reader.close();
    }
}
