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
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public int gameCount() {

        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, Object game) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {

    }
}