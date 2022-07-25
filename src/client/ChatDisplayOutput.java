package client;

import common.Message;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class ChatDisplayOutput extends JFrame {
    private JTextArea messagesArea;
    private JTextField messageField;
    private JButton sendButton;
    private JPanel container;
    private JTextField usernameField;
    private JTextField hostField;
    private JTextField portField;
    private JButton startButton;
    private ServerConnection serverConnection;

    public void print(Message message) {
        String currentText = this.messagesArea.getText();
        currentText += "\n" + message.getUsername() + ": " + message.getBody();

        this.messagesArea.setText(currentText);
    }

    public void setupComponents() {
        this.container = this.setupContainer();
        this.add(container);

        this.container.add(this.setupUsernameField());
        this.container.add(this.setupHostField());
        this.container.add(this.setupPortField());
        this.container.add(this.setupStartButton());

        this.container.add(this.setupMessageArea());
        this.container.add(this.setupMessageField());
        this.container.add(this.setupSendButton());
    }

    private JPanel setupContainer() {
        JPanel container = new JPanel();

        container.setBounds(50, 50, 640, 540);

        this.container = container;

        return container;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    private JButton setupSendButton() {
        JButton sendButton = new JButton("Enviar");
        sendButton.setVisible(false);
        this.sendButton = sendButton;

        sendButton.addActionListener(e -> {
            String rawMessage = this.messageField.getText();
            if (rawMessage == "") {
                return;
            }
            Message message = new Message(this.usernameField.getText(), rawMessage);
            this.serverConnection.sendMessage(message);
            this.messageField.setText("");
        });

        return sendButton;
    }

    private JButton setupStartButton() {
        JButton startButton = new JButton("Conectar");

        startButton.addActionListener(e -> this.connect());
        this.startButton = startButton;

        return startButton;
    }

    private JTextField setupUsernameField() {
        JTextField usernameField = new JTextField();

        usernameField.setEditable(true);
        usernameField.setPreferredSize(new Dimension(640, 50));
        usernameField.setText("username");

        this.usernameField = usernameField;

        return usernameField;
    }

    private JTextField setupPortField() {
        JTextField portField = new JTextField();

        portField.setEditable(true);
        portField.setPreferredSize(new Dimension(640, 50));
        portField.setText("8000");

        this.portField = portField;

        return portField;
    }

    private JTextField setupHostField() {
        JTextField hostField = new JTextField();

        hostField.setEditable(true);
        hostField.setPreferredSize(new Dimension(640, 50));
        hostField.setText("127.0.0.1");

        this.hostField = hostField;

        return hostField;
    }


    private JTextField setupMessageField() {
        JTextField messageField = new JTextField();
        messageField.setEditable(true);
        messageField.setPreferredSize(new Dimension(640, 50));
        messageField.setVisible(false);

        this.messageField = messageField;

        return messageField;
    }

    public JTextArea setupMessageArea() {
        JTextArea messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setPreferredSize(new Dimension(640, 400));
        messagesArea.setVisible(false);

        this.messagesArea = messagesArea;

        return messagesArea;
    }

    public ChatDisplayOutput() {
        this.setLocationRelativeTo(null);
        this.setSize(650, 550);
        this.setResizable(false);

        this.setupComponents();
        this.setVisible(true);
    }

    public void connect() {
        Socket serverSocket = null;
        try {
            serverSocket = new Socket(this.hostField.getText(), Integer.parseInt(this.portField.getText()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.startButton.setVisible(false);
        this.portField.setVisible(false);
        this.hostField.setVisible(false);
        this.usernameField.setVisible(false);

        this.messageField.setVisible(true);
        this.messagesArea.setVisible(true);
        this.sendButton.setVisible(true);

        this.setTitle(getUsername());

        new ServerConnection(serverSocket, this).start();
    }

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
}
