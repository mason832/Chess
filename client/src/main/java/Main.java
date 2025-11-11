import chess.*;
import server.ServerFacade;
import ui.ReadPrintLoop;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerFacade server = new ServerFacade();

        ReadPrintLoop repl = new ReadPrintLoop(server);
        repl.run();
    }
}