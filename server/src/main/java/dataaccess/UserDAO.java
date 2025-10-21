package dataaccess;
import model.UserData;
public interface UserDAO {

    int userCount();

    void addUser(UserData u);

    UserData getUser(String username);

    void clear() throws DataAccessException;
}
