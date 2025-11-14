package server;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var user = new UserData(username, password, email);
        var conn = makeRequest("/user", "POST", user, null);

        // write json body
        try (var out = new OutputStreamWriter(conn.getOutputStream())) {
            gson.toJson(user, out);
        }
        return responseHandling(conn, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        var user = new UserData(username, password, null);
        var conn = makeRequest("/session", "POST", user, null);

        //write json body
        try (var out = new OutputStreamWriter(conn.getOutputStream())) {
            gson.toJson(user, out);
        }
        return responseHandling(conn, AuthData.class);
    }

    public void logout(String authToken) throws Exception {
        var conn = makeRequest("/session", "DELETE", null, authToken);

        if (conn.getResponseCode()==200) {conn.disconnect();}
        else {handleError(conn);}
    }

    public void createGame(String authToken, String gameName) throws Exception{
        var body = new java.util.HashMap<String, String>();
        body.put("gameName", gameName);

        var conn = makeRequest("/game", "POST", body, authToken);

        if (conn.getResponseCode()==200) {
            try(var input = new InputStreamReader(conn.getInputStream())) {
                var response = gson.fromJson(input, java.util.Map.class);
                System.out.println("Game ID: " + response.get("gameID"));
            }
        }
        else {
            handleError(conn);
        }
    }

    public void listGames(String authToken) throws Exception {
        var conn = makeRequest("/game", "GET", null, authToken);

        if (conn.getResponseCode()==200) {
            try (var input = new InputStreamReader(conn.getInputStream())) {
                var response = gson.fromJson(input, java.util.Map.class);
                var games = (java.util.List<java.util.Map<String, Object>>) response.get("games");
                int counter = 1;
                for (var game : games) {
                    var name = game.get("gameName");
                    var white = game.get("whiteUsername");
                    var black = game.get("blackUsername");

                    System.out.printf("Game: "+counter+" | Name: "+name+" | White: "+white+" | Black: "+black+"\n");
                    counter++;
                }
            }
        }
        else {handleError(conn);}
    }

    public void joinGame(int gameID, String authToken,String playerColor) throws Exception {
        var body = new java.util.HashMap<String, Object>();
        body.put("gameID", gameID);
        body.put("playerColor", playerColor);

        var conn = makeRequest("/game", "PUT", body, authToken);

        if (conn.getResponseCode()==200) {conn.disconnect();}
        else {handleError(conn);}
    }

    public void observeGame(int gameID) {
        //add code
    }

    public void clear() throws Exception {
        var url = new URL(serverUrl + "/db");
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        conn.connect();

        var status = conn.getResponseCode();

        if (status != 200) {
            handleError(conn);
        }
    }

    private HttpURLConnection makeRequest(String endpoint, String method,
                                          Object requestBody, String authToken) throws Exception {
        var url = new URL(serverUrl+endpoint);
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-type", "application/json");

        if (authToken!=null) {conn.setRequestProperty("Authorization", authToken);}
        conn.setDoOutput(true);

        if (requestBody!=null){
            try (var out = new OutputStreamWriter(conn.getOutputStream())){
                gson.toJson(requestBody, out);
            }
        }
        return conn;
    }

    private <T> T responseHandling(HttpURLConnection conn, Class<T> responseType) throws Exception {
        if (conn.getResponseCode()==200) {
            try (var input = new InputStreamReader(conn.getInputStream())) {
                return gson.fromJson(input, responseType);
            }
        }
        else {
            handleError(conn);
            return null;
        }
    }

    private void handleError(HttpURLConnection conn) throws Exception {
        String message;
        try(var error = new InputStreamReader(conn.getErrorStream())) {
            message = gson.fromJson(error, String.class);
        }
        catch (Exception e) {
            if (conn.getResponseCode()==400) {message = "bad request";}
            else if (conn.getResponseCode()==401) {message = "unauthorized";}
            else if (conn.getResponseCode()==403) {message = "already taken";}
            else {message = "Error: " + conn.getResponseMessage();}
        }
        throw new Exception(message);
    }

    private ChessGame getGame(String authToken, int gameID) throws Exception {
        var conn = makeRequest("/game", "GET", null, authToken);

        if (conn.getResponseCode()==200) {
            try (var input = new InputStreamReader(conn.getInputStream())) {
                var response = gson.fromJson(input, java.util.Map.class);
                var games = (java.util.List<java.util.Map<String, Object>>) response.get("games");
                ChessGame wantedGame = null;

                for (var game : games) {
                    var id = (double) game.get("gameID");
                    if (id==gameID) {
                        //add code
                    }
                }
                return wantedGame;
            }
        }
        else {
            handleError(conn);
            return null;
        }
    }

}