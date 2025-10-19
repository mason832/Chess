package dataaccess;
import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData user) throws  DataAccessException {
        //check if user already exists and adds them if not
        if (users.containsKey(user.username())) throw new DataAccessException("Error: already taken");
        users.put(user.username(), user);
    }

    //returns a user based on given username
    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    //deletes all users
    @Override
    public void clear() {
        users.clear();
    }
}