package dataaccess;
import model.UserData;
public interface UserDAO {

    int userCount() throws DataAccessException;

    void addUser(UserData u) throws DataAccessException;

    UserData getUser(String username)throws DataAccessException;

    void clear() throws DataAccessException;
}
