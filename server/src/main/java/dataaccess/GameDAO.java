package dataaccess;
import model.GameData;

public interface GameDAO {

    int createGame(String GameName);

    int gameCount();

    GameData getGame(int GameID);

    void clear() throws DataAccessException;

}
