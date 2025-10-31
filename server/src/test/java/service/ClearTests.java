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

    private SQLUserDAO userDAO;
    private SQLAuthDAO authDAO;
    private SQLGameDAO gameDAO;

    @BeforeEach
    public void setup() throws Exception {
        // Initialize the SQL DAOs
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();

        // Clear the database before each test
        clearService = new ClearService(authDAO, gameDAO, userDAO);
        clearService.clear();

        // Create the service layer
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
    }

    @Test
    public void clearPass() throws DataAccessException {
        var authData = userService.register(new UserData("bob", "1234", "bob@email.com"));
        gameService.createGame(authData.authToken(), "testGame");

        assertTrue(userDAO.userCount() > 0);
        assertTrue(authDAO.tokenCount() > 0);
        assertTrue(gameDAO.gameCount() > 0);

        clearService.clear();

        assertEquals(0, userDAO.userCount());
        assertEquals(0, authDAO.tokenCount());
        assertEquals(0, gameDAO.gameCount());
    }
}
