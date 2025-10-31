package dataaccess;
import com.google.gson.Gson;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.*;
import java.sql.*;

public class SQLGameDAO implements GameDAO{

    private final Gson gson = new Gson();
    private final String createGameStatement = """
            CREATE TABLE IF NOT EXISTS gameData (
            id INT AUTO_INCREMENT,
            whiteUsername varchar(20),
            blackUsername varchar(20),
            gameName varchar(20),
            game TEXT,
            PRIMARY KEY (id)
            );""";

    public SQLGameDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createGameStatement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException{
        String sqlStatement = "INSERT INTO gameData (gameName, game) VALUES (?,?)";
        ChessGame newGame = new ChessGame();
        String gameJson = gson.toJson(newGame);

        try (Connection conn = DatabaseManager.getConnection()) {
            try (var prepareStatement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
                prepareStatement.setString(1, gameName);
                prepareStatement.setString(2, gameJson);
                prepareStatement.executeUpdate();

                try (var rs = prepareStatement.getGeneratedKeys()) {
                    if (rs.next()) {return rs.getInt(1);}
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        throw new DataAccessException("Error: couldn't retrieve new game ID");
    }

    @Override
    public int gameCount() {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        String sqlStatement = "SELECT * FROM gameData WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, gameID);
                var result = preparedStatement.executeQuery();

                if (result.next()) {
                    String white = result.getString("whiteUsername");
                    String black = result.getString("blackUsername");
                    String gameName = result.getString("gameName");
                    String gameJson = result.getString("game");
                    ChessGame game = gson.fromJson(gameJson, ChessGame.class);

                    return new GameData(gameID, white, black, gameName, game);
                }
            }
        } catch (SQLException e) {throw new DataAccessException(e.getMessage());}

        return null;
    }

    @Override
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, Object game) throws DataAccessException {
        String sqlStatement = """
                UPDATE gameData
                SET whiteUsername=?, blackUsername=?, gameName=?, game=?
                WHERE id=?
                """;

        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, gameName);

                if (game != null) {
                    String gameJson = gson.toJson(game);
                    preparedStatement.setString(4, gameJson);
                }

                preparedStatement.setInt(5, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {throw new DataAccessException(e.getMessage());}
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {

    }
}