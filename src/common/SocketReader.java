package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketReader {
    private BufferedReader bufferedReader;

    public SocketReader(Socket connection) {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isMessageValid(String message) {
        return (message != null && !message.equals("null"));
    }

    public String read() {
        try {
            String message = this.bufferedReader.readLine();
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
