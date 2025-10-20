package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData auth);

    AuthData getAuth(String authToken);

    void deleteAuth (String authToken);

    void clear() throws DataAccessException;
}
