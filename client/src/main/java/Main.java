import chess.*;
import server.ServerFacade;
import ui.ReadPrintLoop;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerFacade server = new ServerFacade(8080);
        ReadPrintLoop repl = new ReadPrintLoop(server);
        repl.run();
    }
}