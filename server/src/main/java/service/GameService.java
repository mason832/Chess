package service;
import dataaccess.*;
import model.GameData;

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
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        //make sure game name is provided
        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("bad request");
        }

        int gameID = gameDAO.createGame(gameName);

        return new GameData(gameID, null, null, gameName, null);
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        var authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }

        return gameDAO.listGames();
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        //verify authToken
        var authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        //make sure player has a color
        if (playerColor == null || playerColor.isBlank()) {
            throw new DataAccessException("Error: bad request");
        }

        //fetch game
        var game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new DataAccessException("bad request");
        }

        //check if color is taken, throw "already taken" if color is in use
        if (playerColor.equalsIgnoreCase("WHITE") && game.whiteUsername() != null) {
            throw new DataAccessException("already taken");
        }
        if (playerColor.equalsIgnoreCase("BLACK") && game.blackUsername() != null) {
            throw new DataAccessException("already taken");
        }

        if (playerColor.equalsIgnoreCase("WHITE")) {
            gameDAO.updateGame(gameID, authData.username(), game.blackUsername(), game.gameName(), game.game());
        }
        else if (playerColor.equalsIgnoreCase("BLACK")) {
            gameDAO.updateGame(gameID, game.whiteUsername(), authData.username(), game.gameName(), game.game());
        }
        else {
            throw new DataAccessException("bad request");
        }

    }
}
