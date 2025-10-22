package handlers;
import io.javalin.http.Context;
import service.ClearService;
import dataaccess.DataAccessException;


public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }


    public void clear(Context ctx) {
        try {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        }

        catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("unable to clear database");
        }
    }
}
