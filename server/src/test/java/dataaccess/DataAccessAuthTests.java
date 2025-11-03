package dataaccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;


public class DataAccessAuthTests {
    private AuthDAO authDAO;
    private AuthData authData;
    private ClearService clearService;

    @BeforeEach
    public void setup() throws Exception {
        UserDAO userDAO = new SQLUserDAO();
        GameDAO gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
        clearService = new ClearService(authDAO, gameDAO, userDAO);
        UserService userService = new UserService(userDAO, authDAO);
        userDAO.clear();
        authData = userService.register(new UserData("bob", "1234", "bob@email.com"));
    }

    @AfterEach
    public void cleanup() throws DataAccessException {
        clearService.clear();
    }

    @Test
    public void addAuthTestPass() throws DataAccessException {
        authDAO.clear();
        assertEquals(0, authDAO.tokenCount());

        authDAO.addAuth(authData);
        assertEquals(1, authDAO.tokenCount());
    }

    @Test
    public void addAuthTestFail() throws DataAccessException {
        authDAO.clear();
        assertEquals(0, authDAO.tokenCount());
        assertThrows(DataAccessException.class, () -> authDAO.addAuth(new AuthData("", null)));
        assertEquals(0, authDAO.tokenCount());
    }

    @Test
    public void getAuthPass() throws DataAccessException {
        assertEquals(authData, authDAO.getAuth(authData.authToken()));
    }

    @Test
    public void getAuthFail() throws DataAccessException {
        assertNull(authDAO.getAuth(""));
        assertNull(authDAO.getAuth("fakeAuthToken"));
    }

    @Test
    public void deleteAuthPass() throws DataAccessException {
        assertEquals(1, authDAO.tokenCount());
        authDAO.deleteAuth(authData.authToken());
        assertEquals(0, authDAO.tokenCount());
    }

    @Test
    public void deleteAuthFail() throws DataAccessException{
        assertEquals(1, authDAO.tokenCount());
        authDAO.deleteAuth("fakeAuthToken");
        assertEquals(1, authDAO.tokenCount());
    }

    @Test
    public void clearTests() throws DataAccessException {
        assertEquals(1, authDAO.tokenCount());
        authDAO.clear();
        assertEquals(0, authDAO.tokenCount());
    }
}
