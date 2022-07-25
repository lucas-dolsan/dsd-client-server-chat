package server;

import common.Message;
import common.SocketReader;
import common.SocketWriter;
import org.json.JSONObject;

import java.net.Socket;


public class ClientConnection extends Server implements Runnable {
    private Socket socket;
    private SocketReader reader;
    private SocketWriter writer;

    public ClientConnection(Socket clientConnection) {
        this.socket = clientConnection;
        this.reader = new SocketReader(this.socket);
        this.writer = new SocketWriter(this.socket, true);
    }

    public Socket getSocket() {
        return socket;
    }
    public SocketWriter getWriter() {
        return writer;
    }

    private void log(Message message) {
        System.out.println(this.socket.getLocalAddress() + " sent: " + message.getBody());
    }

    @Override
    public void run() {
        String rawMessage;
        while (true) {
            rawMessage = this.reader.read();

            if (!SocketReader.isMessageValid(rawMessage)) {
                break;
            }

            Message message = Message.fromJson(new JSONObject(rawMessage));

            this.log(message);
            this.writer.broadcastMessage(message);
        }
        this.reader.close();
    }
}
