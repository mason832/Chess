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
        if (user.username() == null || user.password() == null || user.email() == null) throw new DataAccessException("Error: bad request");

        //make sure username isn't already taken
        if (userDAO.getUser(user.username())!=null) throw new DataAccessException("Error: already taken");

        //create authorization token
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        authDAO.createAuth(auth);

        return auth;
    }
}