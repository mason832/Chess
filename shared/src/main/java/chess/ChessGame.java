package chess;

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

    ChessBoard chess_board = new ChessBoard();
    TeamColor team_turn = null;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team_turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        team_turn = team;
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
        if (chess_board.getPiece(startPosition) == null) return null;

        var chess_piece = chess_board.getPiece(startPosition);
        var moves = chess_piece.pieceMoves(chess_board, startPosition);

        for (int i = 0; i < moves.size(); i++) {
            //check for check/checkmate and filter any moves that would cause it
        }

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //find king and opposing team
        ChessPosition king_position = null;
        var opposing = new ArrayList<ChessPosition>();
        find_king_and_opposing_team(teamColor, opposing, king_position);

        return false;
    }

    private void find_king_and_opposing_team (TeamColor teamColor, ArrayList<ChessPosition> opposing, ChessPosition king_position) {
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                ChessPiece piece = chess_board.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) opposing.add(new ChessPosition(i,j));
                    else if (piece.getPieceType() == ChessPiece.PieceType.KING) king_position = new ChessPosition(i,j);
                }
            }
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chess_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chess_board;
    }
}