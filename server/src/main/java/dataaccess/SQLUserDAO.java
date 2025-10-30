package dataaccess;

import model.UserData;
import java.sql.Connection;

public class SQLUserDAO implements UserDAO{

    private final String createUserStatement = """
            CREATE TABLE IF NOT EXISTS user (
            'username' varchar(20),
            'password' varchar(50),
            'email' varchar(25),
            PRIMARY KEY ('username')
            """;

    public SQLUserDAO() throws Exception{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createUserStatement)) {
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public int userCount() throws Exception {
        return 0;
    }

    @Override
    public void addUser(UserData u) throws Exception{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?,?,?)")) {
                preparedStatement.setString(1, u.username());
                preparedStatement.setString(2, u.password());
                preparedStatement.setString(3, u.email());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public UserData getUser(String username) throws Exception{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM  user WHERE username=?")) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                }
            }
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
