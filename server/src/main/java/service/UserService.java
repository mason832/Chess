package service;
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
        if (userDAO.getUser(user.username())!=null) throw new DataAccessException("Error: already taken");

        //create user
        userDAO.createUser(user);

        //create authorization token
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(user.username(), authToken);

        //create authData
        authDAO.createAuth(user.username());

        return auth;
    }

    public AuthData login(UserData login_attempt) throws DataAccessException {

        //make sure username and password work
        if (login_attempt.username() == null || login_attempt.username().isEmpty() ||
                login_attempt.password() == null || login_attempt.password().isEmpty()) {
            throw new DataAccessException("bad request");
        }

        UserData account = userDAO.getUser(login_attempt.username());

        if (account == null || !login_attempt.password().equals(account.password())) throw new DataAccessException("unauthorized");

        return authDAO.createAuth(account.username());
    }

    public void logout(String authToken) throws DataAccessException {
        //checks if there's a provided authToken
        if (authToken == null || authToken.isEmpty()) throw new DataAccessException("unauthorized");

        //get authData
        AuthData authData = authDAO.getAuth(authToken);

        //check if authToken is valid
        if (authData == null) throw new DataAccessException("unauthorized");

        //delete autData
        authDAO.deleteAuth(authToken);
    }
}