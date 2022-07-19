package server;

import common.SocketReader;
import common.SocketWriter;

import java.net.Socket;


public class ClientConnection extends Server implements Runnable {
    private Socket socket;
    private SocketReader reader;
    private SocketWriter writer;

    public ClientConnection(Socket clientConnection) {
        this.socket = clientConnection;
        this.reader = new SocketReader(this.socket);
        this.writer = new SocketWriter(this.socket);
    }

    public Socket getSocket() {
        return socket;
    }
    public SocketWriter getWriter() {
        return writer;
    }

    @Override
    public void run() {
        String message;
        while (true) {
            message = this.reader.read();

            if (!SocketReader.isMessageValid(message)) {
                break;
            }

            this.writer.broadcastMessage(message);
        }
        this.reader.close();
    }
}
