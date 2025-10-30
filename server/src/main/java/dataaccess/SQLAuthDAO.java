package dataaccess;

import model.AuthData;
import java.sql.Connection;

public class SQLAuthDAO implements AuthDAO{

    private int authCount = 0;
    private final String createAuthStatement = """
            CREATE TABLE IF NOT EXISTS auth (
            'username' varchar(20),
            'authToken' varchar(50),
            PRIMARY KEY ('authToken')
            """;

    public SQLAuthDAO() throws Exception{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createAuthStatement)) {
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void addAuth(AuthData authData) throws Exception{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (username, authData) VALUES (?,?)")) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        }
        authCount++;
    }

    @Override
    public int tokenCount() {
        return authCount;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {


        authCount--;
    }

    @Override
    public void clear() throws DataAccessException {


        authCount=0;
    }
}
