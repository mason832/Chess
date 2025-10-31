package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void addAuth(AuthData authData) throws Exception;

    int tokenCount();

    AuthData getAuth(String authToken) throws Exception;

    void deleteAuth (String authToken);

    void clear() throws DataAccessException;
}
