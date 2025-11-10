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

    public void run() {
        System.out.println("Welcome to Chess! Type 'help' for commands.");
        while (true) {
            if (loggedIn) {System.out.println("[LOGGED_IN] >>> ");}
            else {System.out.println("[LOGGED_OUT] >>> ");}

            var input = scanner.nextLine().trim().split("\\s+");

            if (!input[0].isBlank()) continue;

            try {
                var command = input[0].toLowerCase();
                if (!loggedIn) {
                    //add code here
                }
            }
        }
    }
}
