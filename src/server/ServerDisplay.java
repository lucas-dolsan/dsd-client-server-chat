package server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDisplay extends JFrame {

    private JPanel container;
    private JTextField portField;
    private JTextField maxClientField;

    private JButton startButton;

    private static ArrayList<ClientConnection> clients = new ArrayList<>();

    public void setupComponents() {
        this.container = this.setupContainer();
        this.add(container);

        this.container.add(this.setupPortField());
        this.container.add(this.setupMaxClientField());
        this.container.add(this.setupStartButton());
    }

    public ServerDisplay() {
        this.setLocationRelativeTo(null);
        this.setSize(660, 200);
        this.setResizable(false);
        this.setTitle("Servidor");

        this.setupComponents();

        this.setVisible(true);
    }

    private JTextField setupPortField() {
        JTextField portField = new JTextField();

        portField.setEditable(true);
        portField.setPreferredSize(new Dimension(640, 50));
        portField.setText("8000");

        this.portField = portField;

        return portField;
    }

    private JTextField setupMaxClientField() {
        JTextField maxClientField = new JTextField();

        maxClientField.setEditable(true);
        maxClientField.setPreferredSize(new Dimension(640, 50));
        maxClientField.setText("3");

        this.maxClientField = maxClientField;

        return maxClientField;
    }

    private JButton setupStartButton() {
        JButton startButton = new JButton("Iniciar");

        startButton.addActionListener(e -> this.onStartButtonClick());
        this.startButton = startButton;

        return startButton;
    }

    private void onStartButtonClick() {
        this.setVisible(false);

        try {
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void start() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(this.getMaxClientCount());

        ServerSocket listener = new ServerSocket(this.getPort());

        System.out.println("Server listening at: " + this.getPort());

        while (true) {
            Socket clientSocket = listener.accept();

            ClientConnection connection = new ClientConnection(clientSocket, this, clients);

            clients.add(connection);

            executor.execute(connection);
        }
    }


    private JPanel setupContainer() {
        JPanel container = new JPanel();

        container.setBounds(50, 50, 640, 540);

        this.container = container;

        return container;
    }

    public int getMaxClientCount() {
        return Integer.parseInt(this.maxClientField.getText());
    }

    public int getPort() {
        return Integer.parseInt(this.portField.getText());
    }

    public void updateLog(String logMessage) {
    }
}
