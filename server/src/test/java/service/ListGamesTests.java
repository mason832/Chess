package service;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;


public class ListGamesTests {
    private UserService userService;
    private GameService gameService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;


    @BeforeEach
    public void setup() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void listGamesSuccess() throws Exception {
        var authData = userService.register(new UserData("bob", "1234", "bob@email.com"));
        Collection<GameData> games = gameService.listGames(authData.authToken());

        assertEquals(0, games.size());

        gameDAO.createGame("Test Game 1");
        gameDAO.createGame("Test Game 2");

        assertEquals(2, games.size());
    }

    @Test
    public void listGameFailUnauthorized() {
        var exception = assertThrows(DataAccessException.class, () -> gameService.listGames("invalid token"));
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}
