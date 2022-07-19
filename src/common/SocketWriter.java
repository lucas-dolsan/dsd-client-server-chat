package common;

import server.ClientConnection;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWriter {
    private PrintWriter printWriter;
    private Socket connection;

    public SocketWriter(Socket connection) {
        try {
            this.connection = connection;
            this.printWriter = new PrintWriter(connection.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String message) {
        this.printWriter.println(message);
    }

    public void broadcastMessage(String message) {
        for (ClientConnection clientConnection : Server.getClients()) {

            Boolean isSelf = clientConnection.getSocket().hashCode() != this.connection.hashCode();

            // so it won't send a message to itself
            if (!isSelf) {
                clientConnection.getWriter().write(message);
            }
        }
    }

}
