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
            //implement catches
        }
    }
}