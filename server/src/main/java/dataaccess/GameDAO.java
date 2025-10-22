package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {

    int createGame(String GameName);

    int gameCount();

    GameData getGame(int GameID);

    Collection<GameData> listGames();

    void clear() throws DataAccessException;

}
