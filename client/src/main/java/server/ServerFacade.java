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
        this.serverUrl = "http://localhost:8080";
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var user = new UserData(username, password, email);

        var url = new URL(serverUrl + "/user");  // ✅ correct endpoint
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // write JSON body
        try (var out = new OutputStreamWriter(conn.getOutputStream())) {
            gson.toJson(user, out);
        }

        int status = conn.getResponseCode();

        if (status == 200) {
            try (var in = new InputStreamReader(conn.getInputStream())) {
                return gson.fromJson(in, AuthData.class);
            }
        } else {
            String message;
            try (var error = new InputStreamReader(conn.getErrorStream())) {
                message = gson.fromJson(error, String.class);
            } catch (Exception e) {
                if (status == 403) {message = "Username already taken";}
                else {message = "Server returned " + status + e.getMessage();}
            }
            throw new Exception(message);
        }
    }

    public AuthData login(String username, String password) {
        //add code
        return null;
    }

}
