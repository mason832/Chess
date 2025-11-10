package ui;
import server.ServerFacade;
import java.util.Scanner;
import model.AuthData;

public class repl {
    private final ServerFacade server;
    private final Scanner scanner = new Scanner(System.in);

    private PreloginClient prelogin;
    private AuthData authData;
    private boolean loggedIn = false;

    public repl(ServerFacade server) {
        this.server = server;
        this.prelogin = new PreloginClient(server);
    }
}
