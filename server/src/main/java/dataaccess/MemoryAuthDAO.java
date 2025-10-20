package dataaccess;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authTokens = new HashMap<>();

    //add new authorization token
    @Override
    public AuthData createAuth(String username) {
        String token = java.util.UUID.randomUUID().toString();
        AuthData auth = new AuthData(username, token);
        authTokens.put(token, auth);
        return auth;
    }

    //get authorization token
    @Override
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    //delete specific authorization token
    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    //delete all authorization tokens
    @Override
    public void clear() throws DataAccessException {
        authTokens.clear();
    }
}
