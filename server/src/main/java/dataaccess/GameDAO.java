package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {

    int createGame(String gameName) throws DataAccessException;

    int gameCount() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, Object game) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void clear() throws DataAccessException;

}
