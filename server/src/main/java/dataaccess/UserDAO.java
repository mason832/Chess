package dataaccess;
import model.UserData;

import java.util.concurrent.ExecutionException;

public interface UserDAO {

    int userCount()throws Exception;

    void addUser(UserData u) throws Exception;

    UserData getUser(String username)throws Exception;

    void clear() throws DataAccessException;
}
