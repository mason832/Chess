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
            //turn json body into a UserData object
            UserData user = gson.fromJson(ctx.body(), UserData.class);

            //call service to register
            AuthData auth = userService.register(user);

            //return json response
            ctx.status(200);
            ctx.result(gson.toJson(auth));

        }
        catch (DataAccessException e) {
            //check if user already exists
            if (e.getMessage().toLowerCase().contains("taken")) {
                ctx.status(403);
                ctx.result(gson.toJson(e.getMessage()));
            }

            else if (e.getMessage().toLowerCase().contains("bad request")) {
                ctx.status(400);
                ctx.result(gson.toJson(e.getMessage()));
            }

            //generic failure
            else {
                ctx.status(500);
                ctx.result(gson.toJson(new ErrorMessage("internal server error")));
            }

            //return a json object for the error
            ctx.result(gson.toJson(new ErrorMessage(e.getMessage())));

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
            if (e.getMessage().contains("unauthorized")) {
                ctx.status(401);
                ctx.result(gson.toJson(e.getMessage()));
            }

            else if (e.getMessage().contains("bad request")) {
                ctx.status(400);
                ctx.result(gson.toJson(e.getMessage()));
            }

            else {
                ctx.status(500);
                ctx.result(gson.toJson(new ErrorMessage("internal server error")));
            }
        }
    }

    public void logout(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200);
        }
        catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                ctx.status(401);
                ctx.result(gson.toJson(e.getMessage()));
            }
            else {
                ctx.status(500);
                ctx.result(gson.toJson(new ErrorMessage("internal server error")));
            }
        }
    }

    // Helper record for JSON error responses
    private record ErrorMessage(String message) {}
}