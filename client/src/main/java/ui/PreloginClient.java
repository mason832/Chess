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
                help - show this list
                """);
    }
}
