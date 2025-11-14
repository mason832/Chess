package service;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;


public class ListGamesTests {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;


    @BeforeEach
    public void setup() throws Exception {
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(authDAO, gameDAO, userDAO);
    }

    @AfterEach
    public void cleanup() throws DataAccessException {
        clearService.clear();
    }

    @Test
    public void listGamesSuccess() throws Exception {
        var authData = userService.register(new UserData("bob", "1234", "bob@email.com"));

        assertEquals(0, gameDAO.gameCount());

        gameDAO.createGame("Test Game 1");
        gameDAO.createGame("Test Game 2");

        assertEquals(2, gameDAO.gameCount());
    }

    @Test
    public void listGameFailUnauthorized() {
        var exception = assertThrows(DataAccessException.class, () -> gameService.listGames("invalid token"));
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}
