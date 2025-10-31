package service;
import org.mindrot.jbcrypt.BCrypt;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import java.util.UUID;


public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        //make sure all information is provided
        if (user.username() == null || user.username().isEmpty() ||
                user.password() == null || user.password().isEmpty() ||
                user.email() == null || user.email().isEmpty())
        {throw new DataAccessException("Error: bad request");}

        //make sure username isn't already taken
        if (userDAO.getUser(user.username())!=null) {
            throw new DataAccessException("Error: already taken");
        }

        //create user
        userDAO.addUser(user);

        //create authData
        AuthData authData = new AuthData(user.username(), UUID.randomUUID().toString());

        //add authData
        authDAO.addAuth(authData);

        return authData;
    }

    public AuthData login(UserData loginAttempt) throws DataAccessException {

        //make sure username and password work
        if (loginAttempt.username() == null || loginAttempt.username().isEmpty() ||
                loginAttempt.password() == null || loginAttempt.password().isEmpty()) {
            throw new DataAccessException("bad request");
        }

        UserData account = userDAO.getUser(loginAttempt.username());

        if (account == null || !BCrypt.checkpw(loginAttempt.password(), account.password())) {
            throw new DataAccessException("unauthorized");
        }

        AuthData authData = new AuthData(account.username(), UUID.randomUUID().toString());
        authDAO.addAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        //checks if there's a provided authToken
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }

        //get authData
        AuthData authData = authDAO.getAuth(authToken);

        //check if authToken is valid
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }

        //delete autData
        authDAO.deleteAuth(authToken);
    }
}