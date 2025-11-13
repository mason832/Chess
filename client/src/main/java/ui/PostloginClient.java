package ui;

import model.AuthData;
import server.ServerFacade;

public class PostloginClient {
    private AuthData authData;
    private ServerFacade server;

    public PostloginClient(ServerFacade server, AuthData authData) {
     this.authData = authData;
     this.server = server;
    }

    public void createGame(String[] input) {
        if (input.length != 2) {
            System.out.println("Usage: create <GAME_NAME>");
            return;
        }

        try {
            server.createGame(authData.authToken(), input[1]);
        }
        catch (Exception e) {
            System.out.println("Create game failed: "+e.getMessage());
        }
    }

    public void list() {}

    public void help() {
        System.out.println("""
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess""");
    }

    public boolean logout () {
        authData = null;
        return false;
    }

}
