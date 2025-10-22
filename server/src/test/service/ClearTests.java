package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTests {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(authDAO,gameDAO,userDAO);
    }

    @Test
    public void clearTest() throws DataAccessException {
        var authData = userService.register(new UserData("bob", "1234", "bob@email.com"));
        gameService.createGame(authData.authToken(), "testGame");

        assertNotEquals(0, authDAO.tokenCount());
        assertNotEquals(0, userDAO.userCount());
        assertNotEquals(0, gameDAO.gameCount());

        clearService.clear();

        assertEquals(0, authDAO.tokenCount());
        assertEquals(0, userDAO.userCount());
        assertEquals(0, gameDAO.gameCount());
    }
}
