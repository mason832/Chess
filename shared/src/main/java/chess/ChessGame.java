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

    ChessBoard chess_board = new ChessBoard();
    TeamColor team_turn = TeamColor.WHITE;
    public ChessGame() {
        chess_board.resetBoard();
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

        setTeamTurn(chess_board.getPiece(startPosition).getTeamColor());
        var chess_piece = chess_board.getPiece(startPosition);
        ArrayList<ChessMove> available_moves = (ArrayList<ChessMove>) chess_piece.pieceMoves(chess_board, startPosition);
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

        for (var move:available_moves) {
            ChessPiece moved_piece = chess_board.getPiece(move.getStartPosition());
            ChessPiece removed_piece = chess_board.getPiece(move.getEndPosition());

            //play hypothetical move
            chess_board.addPiece(move.getEndPosition(), moved_piece);
            chess_board.addPiece(move.getStartPosition(), null);

            if (!isInCheck(team_turn)) valid_moves.add(move);

            //reset board
            chess_board.addPiece(move.getEndPosition(), removed_piece);
            chess_board.addPiece(move.getStartPosition(), moved_piece);
        }

        return valid_moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (chess_board.getPiece(move.getStartPosition()) == null) throw new InvalidMoveException("no piece");
        if (chess_board.getPiece(move.getStartPosition()).getTeamColor() != team_turn) throw new InvalidMoveException("not your turn");
        if (!validMoves(move.getStartPosition()).contains(move)) throw new InvalidMoveException("not a valid move");

        var start = move.getStartPosition();
        var end = move.getEndPosition();
        ChessPiece promotion = new ChessPiece(team_turn, move.getPromotionPiece());

        //make move
        if (promotion.getPieceType() != null) {
            chess_board.addPiece(end, promotion);
            chess_board.addPiece(start, null);
        }
        else {
            chess_board.addPiece(end, chess_board.getPiece(start));
            chess_board.addPiece(start, null);
        }

        if (team_turn == TeamColor.WHITE) team_turn = TeamColor.BLACK;
        else team_turn = TeamColor.WHITE;
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
        var king_position = findKing(teamColor);

        for (var opponent_position : opposition) {
            var piece = chess_board.getPiece(opponent_position);
            var piece_moves = piece.pieceMoves(chess_board, opponent_position);

            for (var move : piece_moves) if (move.getEndPosition().equals(king_position)) return true;
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition king_position = null;
        outer_loop:
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                var piece = chess_board.getPiece(new ChessPosition(i,j));
                if (piece != null && piece.getPieceType().equals(ChessPiece.PieceType.KING) && piece.getTeamColor().equals(teamColor)) {
                    king_position = (new ChessPosition(i,j));
                    break outer_loop;
                }
            }
        }
        return king_position;
    }

    private ArrayList<ChessPosition> findTeam (TeamColor teamColor) {
        var team = new ArrayList<ChessPosition>();
        for (int i=1; i <=8; i++) {
            for (int j=1; j<=8; j++) {
                var piece = chess_board.getPiece(new ChessPosition(i,j));
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chess_board, chessGame.chess_board) && team_turn == chessGame.team_turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chess_board, team_turn);
    }
}