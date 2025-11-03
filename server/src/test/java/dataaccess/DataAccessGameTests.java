package dataaccess;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessGameTests {
    private GameDAO gameDAO;
    private ClearService clearService;
    public GameService gameService;


    @BeforeEach
    public void setup() throws Exception {
        AuthDAO authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        UserDAO userDAO = new SQLUserDAO();
        clearService = new ClearService(authDAO, gameDAO, userDAO);
        gameService = new GameService(gameDAO, authDAO);
    }

    @AfterEach
    public void cleanup() throws DataAccessException {
        clearService.clear();
    }

    @Test
    public void createGamePass() throws DataAccessException{
        assertEquals(0,gameDAO.gameCount());
        gameDAO.createGame("newGame");
        assertEquals(1, gameDAO.gameCount());
    }

    @Test
    public void createGameFail() throws DataAccessException {
        assertEquals(0,gameDAO.gameCount());
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(""));
    }

    @Test
    public void gameCountPass() throws DataAccessException {
        assertEquals(0, gameDAO.gameCount());
        gameDAO.createGame("testGame1");
        assertEquals(1, gameDAO.gameCount());
    }

    @Test
    public void gameCountFail() throws DataAccessException {
        assertEquals(0, gameDAO.gameCount());
        assertNotEquals(1, gameDAO.gameCount());
    }

    @Test
    public void getGamePass() throws DataAccessException {
        int id = gameDAO.createGame("testGame");
        GameData result = gameDAO.getGame(id);
        assertNotNull(result);
    }

    @Test
    public void getGameFail() throws DataAccessException {
        int id = gameDAO.createGame("testGame");
        GameData result = gameDAO.getGame(id+1);
        assertNull(result);
    }

    @Test
    public void updateGamePass() throws DataAccessException {
        int id =gameDAO.createGame("testGame");
        assertEquals("testGame", gameDAO.getGame(id).gameName());
        gameDAO.updateGame(id, "userA", "userB", "UpdatedGame", gameDAO.getGame(id).game());
        assertEquals("UpdatedGame", gameDAO.getGame(id).gameName());
    }

    @Test
    public void updateGameFail() throws DataAccessException {
        int id =gameDAO.createGame("testGame");
        assertEquals("testGame", gameDAO.getGame(id).gameName());
        gameDAO.updateGame(id+1, "userA", "userB", "UpdatedGame", gameDAO.getGame(id).game());
        assertEquals("testGame", gameDAO.getGame(id).gameName());
    }

    @Test
    public void listGamePass() throws DataAccessException {
        assertTrue(gameDAO.listGames().isEmpty());
        gameDAO.createGame("gameA");
        gameDAO.createGame("gameB");
        gameDAO.createGame("gameC");
        assertEquals(3, gameDAO.listGames().size());
    }

    @Test
    public void listGameFail() throws DataAccessException {
        assertTrue(gameDAO.listGames().isEmpty());
        gameDAO.createGame("testGame");
        assertFalse(gameDAO.listGames().isEmpty());
    }

    @Test
    public void clearTest() throws DataAccessException {
        gameDAO.clear();
        assertTrue(gameDAO.listGames().isEmpty());
        gameDAO.createGame("testGame");
        assertFalse(gameDAO.listGames().isEmpty());
        gameDAO.clear();
        assertTrue(gameDAO.listGames().isEmpty());
    }
}
