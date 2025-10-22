package dataAcessTests;
import service.UserService;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LoginTests {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void loginSuccess() throws Exception {
        //register user and get their data
        var authData = userService.register(new UserData("bob", "1234", "bob@email.com"));

        //login
        userService.login(new UserData("bob", "1234", null));

        //check that the authToken exists
        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertNotNull(userDAO.getUser("bob"));
        assertEquals("bob", authDAO.getAuth(authData.authToken()).username());
    }

    @Test
    public void loginFailWrongInfo() throws Exception {
        userService.register(new UserData("bob", "1234", "bob@email.com"));

        var exception = assertThrows(DataAccessException.class, () -> userService.login(new UserData("joe", "1234", null)));
        assertTrue(exception.getMessage().contains("unauthorized"));

        exception = assertThrows(DataAccessException.class, () -> userService.login(new UserData("bob", "password", null)));
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    public void loginFailMissingInfo() throws Exception {
        userService.register(new UserData("bob", "1234", "bob@email.com"));
        DataAccessException exception;

        exception = assertThrows(DataAccessException.class, () -> userService.login(new UserData("", "1234", null)));
        assertTrue(exception.getMessage().contains("bad request"));

        exception = assertThrows(DataAccessException.class, () -> userService.login(new UserData(null, "1234", null)));
        assertTrue(exception.getMessage().contains("bad request"));

        exception = assertThrows(DataAccessException.class, () -> userService.login(new UserData("bob", "", null)));
        assertTrue(exception.getMessage().contains("bad request"));

        exception = assertThrows(DataAccessException.class, () -> userService.login(new UserData("bob", null, null)));
        assertTrue(exception.getMessage().contains("bad request"));
    }
}