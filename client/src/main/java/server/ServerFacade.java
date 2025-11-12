package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade() {
        serverUrl = "http://localhost:8080";
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var user = new UserData(username, password, email);

        var url = new URL(serverUrl + "/user");
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // write json body
        try (var out = new OutputStreamWriter(conn.getOutputStream())) {
            gson.toJson(user, out);
        }
        return ErrorHandling(conn, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        var user = new UserData(username, password, null);

        var url = new URL(serverUrl + "/session");
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        //write json body
        try (var out = new OutputStreamWriter(conn.getOutputStream())) {
            gson.toJson(user, out);
        }
        return ErrorHandling(conn, AuthData.class);
    }

    private <T> T ErrorHandling(HttpURLConnection conn, Class<T> responseType) throws Exception {
        if (conn.getResponseCode()==200) {
            try (var input = new InputStreamReader(conn.getInputStream())) {
                return gson.fromJson(input, responseType);
            }
        }
        else {
            String message;
            try(var error = new InputStreamReader(conn.getErrorStream())) {
                message = gson.fromJson(error, String.class);
            }
            catch (Exception e) {
                message = "Server returned status"+conn.getResponseCode();
            }
            throw new Exception(message);
        }
    }

}
