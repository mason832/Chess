package ui;

import model.AuthData;
import server.ServerFacade;

public class PostloginClient {
    private AuthData authData;

    public PostloginClient(ServerFacade server, AuthData authData) {
     this.authData = authData;
    }

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
