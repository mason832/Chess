package client;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPassTest() {
        //add code
    }

    @Test
    public void registerFailTest() {
        //add code
    }

    @Test
    public void quitPassTest() {
        //add code
    }

    @Test
    public void quitFailTest() {
        //add code
        }

    @Test
    public void loginPassTest() {
        //add code
    }

    @Test
    public void loginFailTest() {
        //add code
    }

    @Test
    public void logoutPassTest() {
        //add code
    }

    @Test
    public void logoutFailTest() {
        //add code
    }

    @Test
    public void createPassTest() {
        //add code
    }

    @Test
    public void createFailTest() {
        //add code
    }

    @Test
    public void listPassTest() {
        //add code
    }

    @Test
    public void listFailTest() {
        //add code
    }

    @Test
    public void playPassTest() {
        //add code
    }

    @Test
    public void playFailTest() {
        //add code
    }

    @Test
    public void observePassTest() {
        //add code
    }

    @Test
    public void observeFailTest() {
        //add code
    }
}
