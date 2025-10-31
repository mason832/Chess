package service;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LogoutTests {
    private UserService userService;
    private ClearService clearService;
    private GameService gameService;
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;

    @BeforeEach
    public void setup() throws Exception {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(authDAO, gameDAO, userDAO);
        clearService.clear();
    }

    @Test
    void logoutSuccess() throws Exception {
        var loginAuthData = userService.register(new UserData("bob", "1234", "bob@email.com"));
        assertEquals(1, authDAO.tokenCount());

        userService.logout(loginAuthData.authToken());
        assertEquals(0, authDAO.tokenCount());
        assertNull(authDAO.getAuth(loginAuthData.authToken()));
    }

    @Test
    void logoutFail() throws Exception{
        userService.register(new UserData("bob", "1234", "bob@email.com"));
        var exception = assertThrows(DataAccessException.class, () -> userService.logout("fakeAuthToken"));

        assertTrue(exception.getMessage().contains("unauthorized"));
        assertEquals(1, authDAO.tokenCount());
    }
}
