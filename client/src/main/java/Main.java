import server.ServerFacade;
import ui.ReadPrintLoop;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        var server = new ServerFacade(port);
        var repl = new ReadPrintLoop(server);
        repl.run();
    }
}
