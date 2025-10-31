package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{

    private final String createAuthStatement = """
            CREATE TABLE IF NOT EXISTS authData (
            username varchar(20),
            authToken varchar(100),
            PRIMARY KEY (authToken)
            )""";

    public SQLAuthDAO() throws Exception{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createAuthStatement)) {
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO authData (username, authToken) VALUES (?,?)")) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int tokenCount() {
        return 0;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, authToken FROM authData WHERE authToken=?")) {
                preparedStatement.setString(2, authToken);
                var rs = preparedStatement.executeQuery();
                if(rs.next()) {
                    String username = rs.getString("username");
                    return new AuthData(username, authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        //add code here
    }

    @Override
    public void clear() throws DataAccessException {
        //add code here
    }
}
