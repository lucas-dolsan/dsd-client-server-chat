package client;

import common.SocketReader;
import common.SocketWriter;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread {
    private Socket serverSocket;
    private SocketReader reader;
    private SocketWriter writer;
    private DisplayOutput displayOutput;

    public ServerConnection(Socket serverSocket, DisplayOutput displayOutput) {
        if(displayOutput == null) {
            throw new RuntimeException("Error: DisplayOutput not properly configured");
        }
        this.displayOutput = displayOutput;
        this.serverSocket = serverSocket;

        this.reader = new SocketReader(this.serverSocket);
        this.writer = new SocketWriter(this.serverSocket);
    }

    public void sendMessage(String message) {
        writer.write(message);
    }

    @Override
    public void run() {
        String message;
        while (true) {
            message = this.reader.read();

            if(!SocketReader.isMessageValid(message)) {
                break;
            }
            this.displayOutput.print(new JSONObject(message).toString());
        }
        this.reader.close();
    }
}
