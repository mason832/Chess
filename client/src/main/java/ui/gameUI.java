package ui;
import chess.*;
import static ui.EscapeSequences.*;

public class gameUI {
    private static final String LIGHT_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    private static final String DARK_SQUARE = SET_BG_COLOR_DARK_GREY;

    private static final String HIGHLIGHT_SQUARE = SET_BG_COLOR_GREEN;
    private static final String MOVE_TARGET_SQUARE = SET_BG_COLOR_DARK_GREEN;

    public static void drawWhitePerspective(ChessGame game) {
        drawBoard(game, ChessGame.TeamColor.WHITE, null);
    }

    public static void drawBlackPerspective(ChessGame game) {
        drawBoard(game, ChessGame.TeamColor.BLACK, null);
    }

    public static void drawBoardWithHighlights(ChessGame game, ChessMove[] highlights, ChessGame.TeamColor perspective) {
        drawBoard(game, perspective, highlights);
    }

    private static void drawBoard(ChessGame game, ChessGame.TeamColor perspective,
                                  ChessMove[] highlightedMoves) {
        //add code
    }
}