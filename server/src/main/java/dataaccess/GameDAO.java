package dataaccess;

public interface GameDAO {

    int createGame(String GameName);

    void clear() throws DataAccessException;

}
