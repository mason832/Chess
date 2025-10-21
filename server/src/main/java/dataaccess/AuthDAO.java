package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void addAuth(AuthData authData);

    int tokenCount();

    AuthData getAuth(String authToken);

    void deleteAuth (String authToken);

    void clear() throws DataAccessException;
}
