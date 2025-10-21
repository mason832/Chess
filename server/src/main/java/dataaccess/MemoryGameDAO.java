package dataaccess;
import model.GameData;
import java.util.HashMap;
import java.util.Map;


public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> games = new HashMap<>();
    private int nextGameID = 0;

    @Override
    public int createGame(String gameName) {
        int id = nextGameID++;
        games.put(id, new GameData(id, null, null, gameName, null));
        return id;
    }

    @Override
    public int gameCount() {
        return games.size();
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void clear() {
        games.clear();
    }
}
