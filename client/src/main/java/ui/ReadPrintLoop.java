package ui;
import server.ServerFacade;

import java.util.Objects;
import java.util.Scanner;
import model.AuthData;

public class ReadPrintLoop {
    private final ServerFacade server;
    private final Scanner scanner = new Scanner(System.in);

    private final PreloginClient prelogin;
    private PostloginClient postlogin;
    private AuthData authData;
    private boolean loggedIn = false;

    public ReadPrintLoop(ServerFacade server) {
        this.server = server;
        this.prelogin = new PreloginClient(server);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess! Type Help to get started.");
        while (true) {
            if (loggedIn) {System.out.print("[LOGGED_IN] >>> ");}
            else {System.out.print("[LOGGED_OUT] >>> ");}

            var input = scanner.nextLine().trim().split("\\s+");

            if (input[0].isBlank()) continue;

            try {
                var command = input[0].toLowerCase();

                if (!loggedIn) {
                    if (preLoginCommands(command, input)) break;
                } else {
                    if (postLoginCommands(command, input)) break;
                }

            } catch (Exception e) {System.out.println("Error: " + e.getMessage());}
        }
    }

    private boolean preLoginCommands(String command, String[] input) {
        switch (command) {
            case "help" -> prelogin.help();

            case "register" -> {
                loggedIn = prelogin.register(input);
                if (loggedIn) {
                    authData = prelogin.getAuthData();
                    postlogin = new PostloginClient(server, authData);
                }
            }
            case "login" -> {
                loggedIn = prelogin.login(input);
                if (loggedIn) {
                    authData = prelogin.getAuthData();
                    postlogin = new PostloginClient(server, authData);
                }
            }
            case "quit" -> {
                System.out.println("さようなら! (goodbye!)");
                return true;
            }
            default -> System.out.println("Command not recognized. Use help for a list of commands.");
        }
        return false;
    }

    private boolean postLoginCommands(String command, String[] input) throws Exception {
        switch(command) {
            case "help" -> postlogin.help();

            case "quit" -> {
                loggedIn = postlogin.logout(authData.authToken());
                System.out.println("さようなら! (goodbye!)");
                return true;
            }

            case "logout" -> loggedIn = postlogin.logout(authData.authToken());

            case "create" -> postlogin.createGame(input);

            case "list" -> postlogin.list(input);

            case "join" -> {}

            case "observe" -> {}

            default -> System.out.println("This is not a recognized command, use help to see a list of available commands");
        }
        return false;
    }
}
