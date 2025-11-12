package ui;
import model.AuthData;
import server.ServerFacade;


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

        var username = input[1];
        var password = input[2];
        var email = input[3];

        try {
            authData = server.register(username, password, email);
            System.out.println("logged in as " + username);
            return true;
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public AuthData login(String[] input, boolean loggedIn) {
        if (input.length != 3 || input[1] == null || input[2] == null) {
            System.out.println("Usage: login <USERNAME> <PASSWORD>");
            return null;
        }

        String username = input[1];
        String password = input[2];

        authData = server.login(username, password);

        if (authData!=null) {
            loggedIn = true;
        } else {
            System.out.println("Unauthorized");
        }
        return authData;
    }


}
