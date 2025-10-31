package service;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CreateGameTests {
    private UserService userService;
    private GameService gameService;
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    @BeforeEach
    public void setup() throws Exception {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void createGameSuccess() throws Exception {
        var authData = userService.register(new UserData("bob", "1234", "bob@email.com"));
        var gameData = gameService.createGame(authData.authToken(), "test Game");

        assertEquals(1, gameDAO.gameCount());
        assertEquals("test Game", gameDAO.getGame(gameData.gameID()).gameName());
    }
}
