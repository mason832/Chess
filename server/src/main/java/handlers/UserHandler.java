package handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import service.UserService;

import javax.xml.crypto.Data;

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
            }

            else if (e.getMessage().toLowerCase().contains("bad request")) ctx.status(400);

            //generic failure
            else ctx.status(500);

            //return a json object for the error
            ctx.result(gson.toJson(new ErrorMessage(e.getMessage())));

        }
    }

    public void login(Context ctx) {
        try {
            //turn json body into a UserData object
            UserData user = gson.fromJson(ctx.body(), UserData.class);

            //make sure username and password work
            if (user.username() == null || user.username().isEmpty() ||
                    user.password() == null || user.password().isEmpty()) {
                ctx.status(400);
                ctx.result(gson.toJson(new ErrorMessage("bad request")));
                return;
            }

            //login
            AuthData auth = userService.login(user);

            //success
            ctx.status(200);
            ctx.result(gson.toJson(auth));
        }
        catch (DataAccessException e) {
            ctx.status(401);
            ctx.result(gson.toJson(new ErrorMessage("unauthorized")));
        }
        catch (Exception e) {
            ctx.status(400);
            ctx.result(gson.toJson(new ErrorMessage("bad request")));
        }
    }

    // Helper record for JSON error responses
    private record ErrorMessage(String message) {}
}