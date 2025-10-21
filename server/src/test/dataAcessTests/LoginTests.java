package dataAcessTests;
import service.UserService;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    private UserService userService;

    @BeforeEach
    public void setup() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
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
        assertEquals("bob", authData.username());
    }

    public void loginFailWrongPassword() throws Exception {
        //write code
    }
}
