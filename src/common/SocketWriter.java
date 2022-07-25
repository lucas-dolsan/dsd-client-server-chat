package common;

import server.ClientConnection;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWriter {
    private PrintWriter printWriter;
    private Socket connection;

    private Boolean shouldBroadcastToSameConnection;

    public SocketWriter(Socket connection, Boolean shouldBroadcastToSameConnection) {
        try {
            this.connection = connection;
            this.printWriter = new PrintWriter(connection.getOutputStream(), true);
            this.shouldBroadcastToSameConnection = shouldBroadcastToSameConnection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String message) {
        this.printWriter.println(message);
    }

    public Boolean isSameConnection(ClientConnection clientConnection) {
        return clientConnection.getSocket().hashCode() != this.connection.hashCode();
    }

    public void log(Message message, ClientConnection clientConnection) {
        System.out.println("Broadcasting: " + "[" + message.getBody() + "]" +  " to: " + clientConnection.getSocket().getLocalAddress());

    }

    public void broadcastMessage(Message message) {
        for (ClientConnection clientConnection : Server.getClients()) {

            if(!this.shouldBroadcastToSameConnection) {
                if (this.isSameConnection(clientConnection)) {
                    break;
                }
            }
            this.log(message, clientConnection);
            clientConnection.getWriter().write(message.toJson().toString());
        }
    }

}
