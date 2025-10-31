package dataaccess;
import org.mindrot.jbcrypt.BCrypt;
import model.UserData;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    private final String createUserStatement = """
            CREATE TABLE IF NOT EXISTS user (
            username varchar(20),
            password varchar(100),
            email varchar(25),
            PRIMARY KEY (username));""";

    public SQLUserDAO() throws Exception {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createUserStatement)) {
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public int userCount() throws DataAccessException {
        var statement = "SELECT COUNT(*) AS count FROM user";
        try (var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(statement);
        var rs = ps.executeQuery()) {
            if (rs.next()) {return rs.getInt("count");
            }
        }catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return 0;
    }

    @Override
    public void addUser(UserData u) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?,?,?)");) {
                String hashedPassword = BCrypt.hashpw(u.password(), BCrypt.gensalt());

                preparedStatement.setString(1, u.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, u.email());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
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
        catch (SQLException e) {throw new DataAccessException(e.getMessage());}
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM user";
        try (var conn = DatabaseManager.getConnection();
        var preparedStatement = conn.prepareStatement(statement);) {preparedStatement.executeUpdate();}
        catch (SQLException e) {throw new DataAccessException(e.getMessage());}
    }
}
