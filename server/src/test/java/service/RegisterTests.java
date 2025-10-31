package service;

import dataaccess.DataAccessException;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    @BeforeEach
    public void setup() throws Exception {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        clearService = new ClearService(authDAO, gameDAO, userDAO);
        userService = new UserService(userDAO, authDAO);
    }

    @AfterEach
    public void reset() throws DataAccessException {
        clearService.clear();
    }

    @Test
    public void registerSuccess() throws Exception {
        var user = new UserData("bob", "1234", "bob@email.com");
        var authData = userService.register(user);
        assertEquals("bob", authDAO.getAuth(authData.authToken()).username());
        assertEquals(1, authDAO.tokenCount());
    }

    @Test
    public void registerFailDuplicate() throws Exception {
        var user = new UserData("bob", "1234", "bob@email.com");
        userService.register(user);
        var exception = assertThrows(DataAccessException.class, () -> userService.register(user));
        assertTrue(exception.getMessage().contains("already taken"));
        assertEquals(1, authDAO.tokenCount());
    }

    @Test
    public void registerFailEmptyUsername() {
        var userA = new UserData("", "1234", "bob@email.com");
        var exceptionA = assertThrows(DataAccessException.class, () -> userService.register(userA));
        assertTrue(exceptionA.getMessage().contains("bad request"));

        var userB = new UserData(null, "1234", "bob@email.com");
        var exceptionB = assertThrows(DataAccessException.class, () -> userService.register(userB));
        assertTrue(exceptionB.getMessage().contains("bad request"));

        try{assertEquals(0, authDAO.tokenCount());}
        catch (DataAccessException e) {}
    }

    @Test
    public void registerFailEmptyPassword() {
        var userA = new UserData("bob", "", "bob@email.com");
        var exceptionA = assertThrows(DataAccessException.class, () -> userService.register(userA));
        assertTrue(exceptionA.getMessage().contains("bad request"));

        var userB = new UserData("bob", null, "bob@email.com");
        var exceptionB = assertThrows(DataAccessException.class, () -> userService.register(userB));
        assertTrue(exceptionB.getMessage().contains("bad request"));

        try{assertEquals(0, authDAO.tokenCount());}
        catch (DataAccessException e) {}
    }

    @Test
    public void registerFailEmptyEmail() {
        var userA = new UserData("bob", "1234", "");
        var exceptionA = assertThrows(DataAccessException.class, () -> userService.register(userA));
        assertTrue(exceptionA.getMessage().contains("bad request"));

        var userB = new UserData("bob", "1234", null);
        var exceptionB = assertThrows(DataAccessException.class, () -> userService.register(userB));
        assertTrue(exceptionB.getMessage().contains("bad request"));

        try{assertEquals(0, authDAO.tokenCount());}
        catch (DataAccessException e) {}
    }
}
