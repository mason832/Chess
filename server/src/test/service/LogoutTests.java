package service;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LogoutTests {
    private UserService userService;
    AuthDAO authDAO;

    @BeforeEach
    public void setup() {
        UserDAO userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void logoutSuccess() throws Exception {
        userService.register(new UserData("bob", "1234", "bob@email.com"));
        var loginAuthData = userService.login(new UserData("bob", "1234", null));
        assertEquals(2, authDAO.tokenCount());

        userService.logout(loginAuthData.authToken());
        assertEquals(1, authDAO.tokenCount());
        assertNull(authDAO.getAuth(loginAuthData.authToken()));
    }

    @Test
    void logoutFail() throws Exception{
        var registerAuthData = userService.register(new UserData("bob", "1234", "bob@email.com"));
        userService.login(new UserData("bob", "1234", null));

        var exception = assertThrows(DataAccessException.class, () -> userService.logout("1"));
        assertTrue(exception.getMessage().contains("unauthorized"));
        assertEquals(2, authDAO.tokenCount());
    }
}
