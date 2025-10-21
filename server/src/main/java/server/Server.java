package server;

import io.javalin.*;
import dataaccess.*;
import handlers.UserHandler;
import service.ClearService;
import service.UserService;
import handlers.ClearHandler;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        //create data access objects
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        //create service object
        UserService userService = new UserService(userDAO, authDAO);
        ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);

        //create handler object
        UserHandler userHandler = new UserHandler(userService);
        ClearHandler clearHandler = new ClearHandler(clearService);

        // Register your endpoints and exception handlers here.
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);
        javalin.delete("/db", clearHandler::clear);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
