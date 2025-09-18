package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

        else if (getPieceType() == PieceType.ROOK) rook_move(board, myPosition, ret);

        else { //queen
            bishop_move(board, myPosition, ret);
            rook_move(board, myPosition, ret);
        }

        return ret;
    }

    //checks if there is a piece present
    private boolean tile_check(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret, int r, int c) {
        if (r <= 8 && r >= 1 && c <= 8 && c >= 1) {

            if (board.getPiece(new ChessPosition(r, c)) != null) {
                if (board.getPiece(new ChessPosition(r, c)).pieceColor != this.pieceColor)
                    ret.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                return true;
            }
            ret.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
            return false;
        }
        return false;
    }

    private void bishop_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        int c = myPosition.getColumn();
        for (int r = myPosition.getRow() + 1; r != 9; r++) {
            if (++c > 8 || tile_check(board, myPosition, ret, r, c)) break;
        }

        c = myPosition.getColumn();
        for (int r = myPosition.getRow() + 1; r != 9; r++) {
            if (--c == 0|| tile_check(board, myPosition, ret, r, c)) break;
        }

        c = myPosition.getColumn();
        for (int r = myPosition.getRow() - 1; r != 0; r--) {
            if (++c > 8|| tile_check(board, myPosition, ret, r, c)) break;
        }

        c = myPosition.getColumn();
        for (int r = myPosition.getRow() - 1; r != 0; r--) {
            if (--c == 0|| tile_check(board, myPosition, ret, r, c)) break;
        }
    }

    private void king_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {

        //check top
        tile_check(board, myPosition, ret, myPosition.getRow() + 1, myPosition.getColumn() - 1);
        tile_check(board, myPosition, ret, myPosition.getRow() + 1, myPosition.getColumn());
        tile_check(board, myPosition, ret, myPosition.getRow() + 1, myPosition.getColumn() + 1);


        //check sides
        tile_check(board, myPosition, ret, myPosition.getRow(), myPosition.getColumn()+1);
        tile_check(board, myPosition, ret, myPosition.getRow(), myPosition.getColumn()-1);

        //check below
        tile_check(board, myPosition, ret, myPosition.getRow()-1, myPosition.getColumn()+1);
        tile_check(board, myPosition, ret, myPosition.getRow()-1, myPosition.getColumn());
        tile_check(board, myPosition, ret, myPosition.getRow()-1, myPosition.getColumn()-1);

    }

    private void knight_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        //add code here
    }

    private void pawn_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        //add code here
    }

    private void rook_move(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> ret) {
        //add code here
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}