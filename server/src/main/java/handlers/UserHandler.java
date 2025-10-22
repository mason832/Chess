package handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import service.UserService;
import java.util.Map;


public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) {
        try {
            //turn json body into a UserData object
            UserData user = gson.fromJson(ctx.body(), UserData.class);

            //call service to register
            AuthData auth = userService.register(user);

            //return json response
            ctx.status(200);
            ctx.result(gson.toJson(auth));

        }
        catch (DataAccessException e) {
            handleErrors(ctx, e);
        }
    }

    public void login(Context ctx) {
        try {
            //turn json body into a UserData object
            UserData user = gson.fromJson(ctx.body(), UserData.class);

            //login
            AuthData auth = userService.login(user);

            //success
            ctx.status(200);
            ctx.result(gson.toJson(auth));
        }
        catch (DataAccessException e) {
            handleErrors(ctx, e);
        }
    }

    public void logout(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            userService.logout(authToken);
            ctx.status(200);
            ctx.result("{}");
        }

        catch (DataAccessException e) {
            handleErrors(ctx, e);
        }
    }

    private void handleErrors(Context ctx, DataAccessException e) {
        String message = e.getMessage().toLowerCase();

        if (message.contains("unauthorized")) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", "Error: unauthorized")));
        }
        else if (message.contains("bad request")) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", "Error: bad request")));
        }
        else if (message.contains("taken")) {
            ctx.status(403);
            ctx.result(gson.toJson(Map.of("message", "Error: already taken")));
        }
        else {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }

    // Helper record for JSON error responses
    private record ErrorMessage(String message) {}
}