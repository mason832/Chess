package server;

import handlers.GameHandler;
import io.javalin.*;
import dataaccess.*;
import handlers.UserHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import handlers.ClearHandler;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        //create data access objects
        UserDAO userDAO;
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        try {userDAO = new SQLUserDAO();}
        catch (Exception e) {userDAO = new MemoryUserDAO();}


        //create service object
        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);

        //create handler object
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);
        ClearHandler clearHandler = new ClearHandler(clearService);

        // Register your endpoints and exception handlers here.
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);
        javalin.delete("/db", clearHandler::clear);
        javalin.post("/game", gameHandler::createGame);
        javalin.get("/game",gameHandler::listGames);
        javalin.put("/game", gameHandler::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
