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
            if (loggedIn) {System.out.println("[LOGGED_IN] >>> ");}
            else {System.out.println("[LOGGED_OUT] >>> ");}

            var input = scanner.nextLine().trim().split("\\s+");

            if (!input[0].isBlank()) continue;

            try {
                var command = input[0].toLowerCase();

                if (!loggedIn && preLoginCommands(command, input)) {break;}
                else if (postLoginCommands(command, input)) break;

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
                //add code
            }
            case "quit" -> {
                System.out.println("さようなら! (goodbye!)");
                return true;
            }
            case null, default -> System.out.println("Command not recognized. Use help for a list of commands.");
        }
        return false;
    }

    private boolean postLoginCommands(String command, String[] input) {
        if (Objects.equals(command, "quit")) {
            System.out.println("さようなら! (goodbye!)");
            return true;
        }
        else {System.out.println("Command not recognized. Use help for a list of commands.");}
        return false;
    }
}
