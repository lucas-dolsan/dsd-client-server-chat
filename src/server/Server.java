package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int PORT = 8000;
    public static final int MAX_CLIENT_COUNT = 5;

    private static ArrayList<ClientConnection> clients = new ArrayList<>();
    private static ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENT_COUNT);
    public static ArrayList<ClientConnection> getClients() {
        return clients;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Server listening at: " + PORT);

        while (true) {
            Socket clientSocket = listener.accept();

            ClientConnection connection = new ClientConnection(clientSocket);

            clients.add(connection);
            executor.execute(connection);
        }
    }
}
