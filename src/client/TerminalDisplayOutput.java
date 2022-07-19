package client;

public class TerminalDisplayOutput extends DisplayOutput {

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
