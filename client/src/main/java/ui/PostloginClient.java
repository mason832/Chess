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

    public void list(String[] input) {
        if (input.length!=1) {
            System.out.println("Usage: list");
            return;
        }

        try {server.listGames(authData.authToken());}
        catch(Exception e) {System.out.println("List games failed: " + e.getMessage());}
    }

    public void help() {
        System.out.println("""
                create <GAME_NAME> - a game
                list - games
                join <GAME_NUMBER> [WHITE|BLACK] - a game
                observe <GAME_NUMBER> - a game (unimplemented)
                logout - when you are done
                quit - playing chess""");
    }

    public boolean logout (String authToken) throws Exception {
        server.logout(authToken);
        return false;
    }

    public void joinGame(String[] input) {
        if (input.length != 3) {
            System.out.println("Usage: join <GAME_NUMBER> <WHITE|BLACK>");
            return;
        }

        try {
            int gameID = Integer.parseInt(input[1]);
            String color = input[2].toUpperCase();

            if (!color.equals("WHITE") && !color.equals("BLACK")) {
                System.out.println("Color must be WHITE or BLACK");
                return;
            }

            server.joinGame(gameID, authData.authToken(), color);
            System.out.println("Joined game " + gameID + " as " + color);
        }
        catch (Exception e) {
            System.out.println("Join failed: " + e.getMessage());
        }
    }

    public void observeGame(String[] input) {
        if (input.length!=2) {
            System.out.println("Usage: observe <GAME_NUMBER>");
            return;
        }

        try {
            int gameID = Integer.parseInt(input[1]);

            server.observeGame(authData.authToken(), gameID);
        }
        catch (Exception e) {System.out.println("observation failed: " + e.getMessage());}
    }
}
