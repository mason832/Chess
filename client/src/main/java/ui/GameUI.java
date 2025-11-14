package ui;
import chess.*;
import static ui.EscapeSequences.*;

public class GameUI {

    public void drawBoard(ChessGame game, String playerColor) {
        if (playerColor.equalsIgnoreCase("WHITE")) {drawWhitePerspective(game);}
        else {drawBlackPerspective(game);}
    }

    private void drawWhitePerspective(ChessGame game) {
        System.out.println("this is a board drawn from white's perspective");
    }

    private void drawBlackPerspective(ChessGame game) {
        System.out.println("this is a board drawn from black's perspective");
    }
}