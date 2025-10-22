package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard chessBoard = new ChessBoard();
    TeamColor teamTurn = TeamColor.WHITE;
    public ChessGame() {
        chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (chessBoard.getPiece(startPosition) == null) {
            return null;
        }

        setTeamTurn(chessBoard.getPiece(startPosition).getTeamColor());
        var chessPiece = chessBoard.getPiece(startPosition);
        ArrayList<ChessMove> availableMoves = (ArrayList<ChessMove>) chessPiece.pieceMoves(chessBoard, startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        for (var move:availableMoves) {
            ChessPiece movedPiece = chessBoard.getPiece(move.getStartPosition());
            ChessPiece removedPiece = chessBoard.getPiece(move.getEndPosition());

            //play hypothetical move
            chessBoard.addPiece(move.getEndPosition(), movedPiece);
            chessBoard.addPiece(move.getStartPosition(), null);

            if (!isInCheck(teamTurn)) {
                validMoves.add(move);
            }

            //reset board
            chessBoard.addPiece(move.getEndPosition(), removedPiece);
            chessBoard.addPiece(move.getStartPosition(), movedPiece);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (chessBoard.getPiece(move.getStartPosition()) == null) throw new InvalidMoveException("no piece");
        if (chessBoard.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) throw new InvalidMoveException("not your turn");
        if (!validMoves(move.getStartPosition()).contains(move)) throw new InvalidMoveException("not a valid move");

        var start = move.getStartPosition();
        var end = move.getEndPosition();
        ChessPiece promotion = new ChessPiece(teamTurn, move.getPromotionPiece());

        //make move
        if (promotion.getPieceType() != null) {
            chessBoard.addPiece(end, promotion);
            chessBoard.addPiece(start, null);
        }
        else {
            chessBoard.addPiece(end, chessBoard.getPiece(start));
            chessBoard.addPiece(start, null);
        }

        if (teamTurn == TeamColor.WHITE) teamTurn = TeamColor.BLACK;
        else teamTurn = TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessPosition> opposition = null;
        if (teamColor.equals(TeamColor.WHITE)) opposition = findTeam(TeamColor.BLACK);
        else opposition = findTeam(TeamColor.WHITE);
        var kingPosition = findKing(teamColor);

        for (var opponentPosition : opposition) {
            var piece = chessBoard.getPiece(opponentPosition);
            var pieceMoves = piece.pieceMoves(chessBoard, opponentPosition);

            for (var move : pieceMoves)
                if (move.getEndPosition().equals(kingPosition)) {
                    return true;
                }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        outer_loop:
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                var piece = chessBoard.getPiece(new ChessPosition(i,j));
                if (piece != null && piece.getPieceType().equals(ChessPiece.PieceType.KING) && piece.getTeamColor().equals(teamColor)) {
                    kingPosition = (new ChessPosition(i,j));
                    break outer_loop;
                }
            }
        }
        return kingPosition;
    }

    private ArrayList<ChessPosition> findTeam (TeamColor teamColor) {
        var team = new ArrayList<ChessPosition>();
        for (int i=1; i <=8; i++) {
            for (int j=1; j<=8; j++) {
                var piece = chessBoard.getPiece(new ChessPosition(i,j));
                if (piece != null && piece.getTeamColor().equals(teamColor)) team.add(new ChessPosition(i,j));
            }
        }
        return team;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) return false;

        var team = findTeam(teamColor);

        for (var position : team) {
            var moves = validMoves(position);
            if (!moves.isEmpty()) return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;

        var team = findTeam(teamColor);

        for (var position : team) {
            var moves = validMoves(position);
            if (!moves.isEmpty()) return false;
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, teamTurn);
    }
}