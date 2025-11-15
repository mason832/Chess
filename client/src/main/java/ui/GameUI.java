package ui;
import chess.*;
import static ui.EscapeSequences.*;

public class GameUI {

    public void drawBoard(ChessGame game, String playerColor) {
        if (playerColor.equalsIgnoreCase("WHITE")) {drawWhitePerspective(game);}
        else {drawBlackPerspective(game);}
        reset();
    }

    private void drawWhitePerspective(ChessGame game) {
        ChessBoard board = game.getBoard();

        System.out.println("\n\n    a   b   c  d   e  f   g   h");
        for (int row = 8; row >= 1; row--) {
            System.out.print(" " + row + " ");
            for (int col = 1; col <= 8; col++) {
                createTile(board.getPiece(new ChessPosition(row, col)), row, col);
            }
            System.out.println(" " + row);
        }
        System.out.println("    a   b   c  d   e  f   g   h");
    }

    private void drawBlackPerspective(ChessGame game) {
        ChessBoard board = game.getBoard();

        System.out.println("    h   g   f   e   d   c   b   a");
        for (int row = 1; row <= 8; row++) {
            System.out.print(" " + row + " ");
            for (int col = 8; col >= 1; col--) {
                createTile(board.getPiece(new ChessPosition(row, col)),
                        row, col);
            }
            System.out.println(" " + row);
        }
        System.out.println("    h   g   f   e   d   c   b   a");}

    private void reset() {
        System.out.println(RESET_BG_COLOR);
        System.out.println(RESET_TEXT_COLOR);
    }

    private void createTile(ChessPiece piece, int row, int col) {
        boolean darkSquare = (row + col) % 2 == 0;

        if (!darkSquare) System.out.print(SET_BG_COLOR_LIGHT_GREY);
        else System.out.print(SET_BG_COLOR_DARK_GREY);

        if (piece == null) {
            System.out.print(EMPTY);
        } else {
            System.out.print(pieceToText(piece));
        }

        System.out.print(RESET_BG_COLOR);
    }

    private String pieceToText(ChessPiece piece) {
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();

        if (color == ChessGame.TeamColor.WHITE) {
            return switch (type) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        } else {
            return switch (type) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }
    }
}