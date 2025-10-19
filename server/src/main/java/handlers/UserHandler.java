package handlers;
import com.google.gson.Gson;
import io.javalin.http.Context;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import service.UserService;


public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) {
        try {
            UserData user = gson.fromJson(ctx.body(), UserData.class);
            AuthData auth = userService.register(user);

            ctx.status(200);
            ctx.json(auth);
        }

        catch (DataAccessException exception) {
            //not all information provided
            if (exception.getMessage().contains("bad request")) ctx.status(400);

            //username is already taken
            else if (exception.getMessage().contains("already taken")) ctx.status(403);

            //internal failure
            else ctx.status(500);

            ctx.json(java.util.Map.of("message", exception.getMessage()));
        }

        catch (Exception exception) {
            ctx.status(500);
            ctx.json(java.util.Map.of("message", "Error: " + exception.getMessage()));
        }
    }
}