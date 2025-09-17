package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var ret = new ArrayList<ChessMove>();
        if (getPieceType() == PieceType.BISHOP) bishop_move(board, myPosition, ret);

        else if (getPieceType() == PieceType.KING) king_move(board, myPosition, ret);

        else if (getPieceType() == PieceType.KNIGHT) knight_move(board, myPosition, ret);

        else if (getPieceType() == PieceType.PAWN) pawn_move(board, myPosition, ret);

        if (getPieceType() == PieceType.ROOK) rook_move(board, myPosition, ret);

        //assume piece is queen
        else {
            bishop_move(board, myPosition, ret);
            rook_move(board, myPosition, ret);
        }

        return ret;
    }

    private static void bishop_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        int c = myPosition.getColumn();
        for (int r = myPosition.getRow() + 1; r != 9; r++) {
            if (++c > 8) break;
            ret.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
        }

        c = myPosition.getColumn();
        for (int r = myPosition.getRow() + 1; r != 9; r++) {
            if (--c == 0) break;
            ret.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
        }

        c = myPosition.getColumn();
        for (int r = myPosition.getRow() - 1; r != 0; r--) {
            if (++c > 8) break;
            ret.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
        }

        c = myPosition.getColumn();
        for (int r = myPosition.getRow() - 1; r != 0; r--) {
            if (--c == 0) break;
            ret.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
        }
    }

    private static void king_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        //add code here
    }

    private static void knight_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        //add code here
    }

    private static void pawn_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        //add code here
    }

    private static void rook_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        //add code here
    }
}