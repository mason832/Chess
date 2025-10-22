package handlers;
import com.google.gson.Gson;
import io.javalin.http.Context;
import service.GameService;
import dataaccess.DataAccessException;
import java.util.Map;

public class GameHandler {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void createGame(Context ctx) {
        try {
            Map request = gson.fromJson(ctx.body(), Map.class);

            Object nameRequest = request.get("gameName");
            if (nameRequest == null || nameRequest.toString().isEmpty()) {
                ctx.status(400);
                ctx.result(gson.toJson(Map.of("message", "Error: bad request")));
                return;
            }

            String gameName = nameRequest.toString();
            String authToken = ctx.header("Authorization");

            var game = gameService.createGame(authToken, gameName);

            ctx.status(200);
            ctx.result(gson.toJson(Map.of("gameID", game.gameID())));
        }
        catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: unauthorized")));

            }

            else if (e.getMessage().contains("bad request")) {
                ctx.status(400);
                ctx.result(gson.toJson(Map.of("message", "Error: bad request")));

            }

            else {
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            }
        }
    }

    public void listGames(Context ctx) {
        try {
            String authToken = ctx.header ("Authorization");
            var games = gameService.listGames(authToken);

            ctx.status(200);
            ctx.result(gson.toJson(Map.of("games", games)));
        }
        catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
            else {
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            }
        }
    }

    public void joinGame(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            Map request = gson.fromJson(ctx.body(), Map.class);

            int gameID = ((Double) request.get("gameID")).intValue();
            String playerColor = (String) request.get("playerColor");

            gameService.joinGame(authToken, gameID, playerColor);

            ctx.status(200);
            ctx.result("{}");
        }
        catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
            else if (e.getMessage().toLowerCase().contains("bad request")
                    || e.getMessage().toLowerCase().contains("invalid")
                    || e.getMessage().toLowerCase().contains("not found")) {
                ctx.status(400);
                ctx.result(gson.toJson(Map.of("message", "Error: bad request")));
            }

            else if (e.getMessage().contains("taken")) {
                ctx.status(403);
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
            else {
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            }
        }
    }
}
