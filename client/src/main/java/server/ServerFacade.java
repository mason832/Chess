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

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws Exception{
        var user = new UserData(username, password, email);

        var url = new URL(serverUrl+"/user");
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        //write Json body
        try (var out = new OutputStreamWriter(conn.getOutputStream())) {
            gson.toJson(user, out);
        }

        //error handling
        var status = conn.getResponseCode();

        if (status == 200) {
            try (var in = new InputStreamReader(conn.getErrorStream())) {
                return gson.fromJson(in, AuthData.class);
            }
        }

        else {
            String message;
            try (var error = new InputStreamReader(conn.getErrorStream())) {
                message = gson.fromJson(error, String.class);
            } catch (Exception e) {
                message = "Server returned status " + status;
            } throw new Exception(message);
        }
    }
}