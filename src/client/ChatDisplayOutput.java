package client;

import common.Message;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatDisplayOutput extends JFrame {
    private JTextArea messagesArea;
    private JTextField messageField;
    private JButton sendButton;
    private JPanel container;
    private ServerConnection serverConnection;

    public void print(String message) {
        String currentText = this.messagesArea.getText();
        currentText += "\n" + message;
        this.messagesArea.setText(currentText);
    }

    public void setupComponents() {
        this.container = this.setupContainer();
        this.add(container);

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

    private JButton setupSendButton() {
        JButton sendButton = new JButton("Enviar");

        sendButton.addActionListener(e -> {
            String rawMessage = this.messageField.getText();
            if (rawMessage == "") {
                return;
            }
            this.serverConnection.sendMessage(new Message(rawMessage));
            this.messageField.setText("");
        });

        return sendButton;
    }

    private JTextField setupMessageField() {
        JTextField messageField = new JTextField();
        messageField.setEditable(true);
        messageField.setPreferredSize(new Dimension(640, 50));

        this.messageField = messageField;

        return messageField;
    }

    public JTextArea setupMessageArea() {
        JTextArea messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setPreferredSize(new Dimension(640, 400));

        this.messagesArea = messagesArea;

        return messagesArea;
    }

    public ChatDisplayOutput() {
        this.setLocationRelativeTo(null);
        this.setSize(650, 550);
        this.setResizable(false);

        this.setupComponents();

    }

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
}
