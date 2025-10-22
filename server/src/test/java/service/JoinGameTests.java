package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class JoinGameTests {
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameService gameService;
    private UserService userService;
    private AuthData authData;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        authData = userService.register(new UserData("bob", "1234", "bob@email.com"));
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        //create game
        var game = gameService.createGame(authData.authToken(), "gameA");

        //join game
        gameService.joinGame(authData.authToken(), game.gameID(), "white");

        //make sure game info is updated
        var updated = gameDAO.getGame(game.gameID());
        assertEquals("bob", updated.whiteUsername());
        assertEquals("gameA", updated.gameName());
        assertNull(updated.blackUsername());
    }

    @Test
    public void joinGameFailUnauthorized() {
        var exception = assertThrows(DataAccessException.class, () -> gameService.joinGame("invalid token", 1, "white"));

        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}
