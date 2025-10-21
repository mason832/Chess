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
        return id;
    }

    @Override
    public void clear() {
        games.clear();
    }
}
