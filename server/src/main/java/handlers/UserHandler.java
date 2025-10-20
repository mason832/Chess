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
            // Turn JSON body into a UserData object
            UserData user = gson.fromJson(ctx.body(), UserData.class);

            // Call service to register
            AuthData auth = userService.register(user);

            // If successful, return JSON response
            ctx.status(200);
            ctx.result(gson.toJson(auth));

        } catch (DataAccessException e) {
            // check if user already exists
            if (e.getMessage().toLowerCase().contains("already exists")) ctx.status(403);

            //generic failure
            else ctx.status(500);

            // Return a json object for the error
            ctx.result(gson.toJson(new ErrorMessage(e.getMessage())));

        } catch (Exception e) {
            ctx.status(400);
            ctx.result(gson.toJson(new ErrorMessage("Error: bad request")));
        }
    }

    // Helper record for JSON error responses
    private record ErrorMessage(String message) {}
}