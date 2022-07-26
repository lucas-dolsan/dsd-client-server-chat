package server;

import common.Message;
import common.SocketReader;
import common.SocketWriter;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;


public class ClientConnection implements Runnable {
    private final ArrayList<ClientConnection> clients;
    private ServerDisplay serverDisplay;
    private Socket socket;
    private SocketReader reader;
    private SocketWriter writer;

    private String username;

    public ClientConnection(Socket clientConnection, ServerDisplay serverDisplay, ArrayList<ClientConnection> clients) {
        this.clients = clients;
        this.socket = clientConnection;
        this.serverDisplay = serverDisplay;
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
        String logMessage = this.socket.getLocalAddress() + " sent: " + message.getBody();
        System.out.println(logMessage);
        this.serverDisplay.updateLog(logMessage);
    }

    public Message buildHandshakeResponse(Message originalMessage) {
        String newUserConnectedMessage = originalMessage.getUsername() + " conectou";
        return new Message("Server", newUserConnectedMessage);
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

            if(message.getHandshake()) {
                this.username = message.getUsername();

                Message handshakeResponse = this.buildHandshakeResponse(message);
                this.writer.broadcastMessage(handshakeResponse, this.clients);
            } else {
                this.writer.broadcastMessage(message, this.clients);
            }
        }
        this.reader.close();
    }
}
