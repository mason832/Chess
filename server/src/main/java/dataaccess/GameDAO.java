package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {

    int createGame(String GameName);

    int gameCount();

    GameData getGame(int GameID);

    void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, Object game) throws DataAccessException;

    Collection<GameData> listGames();

    void clear() throws DataAccessException;

}
