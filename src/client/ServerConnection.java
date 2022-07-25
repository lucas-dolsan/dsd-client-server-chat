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
    private ChatDisplayOutput displayOutput;

    public ServerConnection(Socket serverSocket, ChatDisplayOutput displayOutput) {
        if(displayOutput == null) {
            throw new RuntimeException("Error: DisplayOutput not properly configured");
        }
        this.displayOutput = displayOutput;
        this.serverSocket = serverSocket;

        this.reader = new SocketReader(this.serverSocket);
        this.writer = new SocketWriter(this.serverSocket, false);

        this.displayOutput.setServerConnection(this);
    }

    public void sendMessage(Message message) {
        System.out.println("sendMessage: " + message.getBody());

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
            this.displayOutput.print(message.getBody());
        }
        this.reader.close();
    }
}
