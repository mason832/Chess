package client;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import model.AuthData;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterEach
    void reset() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer(){
        server.stop();
    }


    @Test
    public void registerPassTest() throws Exception {
        var authData = facade.register("joe", "1234", "joe@email.com");

        assertNotNull(authData);
        assertNotNull(authData.authToken());
    }

    @Test
    public void registerFailTest() throws Exception {
        facade.register("joe", "1234", "joe@email.com");

        Exception e = assertThrows(Exception.class, () ->
                facade.register("joe", "1234", "joe@email.com"));

        assertTrue(e.getMessage().contains("already taken"));

        e = assertThrows(Exception.class, () ->
                facade.register("","",""));

        assertTrue(e.getMessage().contains("bad request"));
    }

    @Test
    public void loginPassTest() throws Exception {
        facade.register("joe", "1234", "joe@email.com");

        AuthData authData = facade.login("joe", "1234");

        assertNotNull(authData);
    }

    @Test
    public void loginFailTest() throws Exception{
        facade.register("joe", "1234", "joe@email.com");

        Exception e = assertThrows(Exception.class, () ->
                facade.login("judy", "1234"));

        assertTrue(e.getMessage().contains("unauthorized"));

        e = assertThrows(Exception.class, () ->
                facade.login("joe", "123"));

        assertTrue(e.getMessage().contains("unauthorized"));
    }

    @Test
    public void logoutPassTest()throws Exception {
        //add code
    }

    @Test
    public void logoutFailTest()throws Exception {
        //add code
    }

    @Test
    public void createPassTest()throws Exception {
        //add code
    }

    @Test
    public void createFailTest()throws Exception {
        //add code
    }

    @Test void clear() throws Exception {}

}
