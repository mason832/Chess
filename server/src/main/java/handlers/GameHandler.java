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

            String gameName = request.get("gameName").toString();
            String authToken = ctx.header("Authorization");

            var game = gameService.createGame(authToken, gameName);

            ctx.status(200);
            ctx.result(gson.toJson(Map.of("gameID", game.gameID())));
        }
        catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) ctx.status(401);

            else if (e.getMessage().contains("bad request")) ctx.status(400);

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
            if (e.getMessage().contains("unauthorized")) ctx.status(401);
            else {
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            }
        }
    }

    public void joinGame(Context ctx) {
        //add code here
    }
}
