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
