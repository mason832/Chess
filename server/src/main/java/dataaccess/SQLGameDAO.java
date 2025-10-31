package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{
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