package ui;
import model.AuthData;
import server.ServerFacade;

import java.util.Objects;


public class PreloginClient {
    private final ServerFacade server;
    private AuthData authData;

    public PreloginClient(ServerFacade server) {
        this.server = server;
    }

    public void help() {
        System.out.println("""
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - exit the program
                """);
    }

    public AuthData getAuthData() {
        return authData;
    }

    public boolean register(String[] input) {
        if (!(input.length == 4)) {
            System.out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
            return false;
        }

        return responseHandling("register", input);
    }

    public boolean login(String[] input) {
        if (input.length != 3) {
            System.out.println("Usage: login <USERNAME> <PASSWORD>");
            return false;
        }

        return responseHandling("login", input);
    }

    private boolean responseHandling(String command, String[] input) {
        try {
            if (Objects.equals(command, "register")) {authData = server.register(input[1], input[2], input[3]);}
            else if (Objects.equals(command, "login")) {authData = server.login(input[1], input[2]);}
            System.out.println("Logged in as " + input[1]);
            return true;
        }
        catch(Exception e) {
            System.out.println(command + " failed: " + e.getMessage());
            return false;
        }
    }
}
