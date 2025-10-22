package service;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import java.util.UUID;
import java.util.Collection;


public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        //check AuthToken
        var authData = authDAO.getAuth(authToken);
        if (authData == null) throw new DataAccessException("Error: unauthorized");

        //make sure game name is provided
        if (gameName == null || gameName.isEmpty()) throw new DataAccessException("bad request");

        int gameID = gameDAO.createGame(gameName);

        return new GameData(gameID, null, null, gameName, null);
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException{
        var authData = authDAO.getAuth(authToken);
        if (authData == null) throw new DataAccessException("unauthorized");

        return gameDAO.listGames();
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        //add code here
    }
}
