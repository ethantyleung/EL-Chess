package chess.players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import chess.Type;
import chess.board.Board;
import chess.board.Move;
import chess.board.Move.KingSideCastle;
import chess.pieces.King;
import chess.pieces.Piece;

public abstract class Player {
    
    protected final Collection<Move> allLegalMoves;

    protected final Board board;

    protected final King theKingPiece;

    private final boolean isChecked;
    
    public Player(final Board board, final Collection<Move> allLegalMoves, final Collection<Move> allOpposingMoves) {
        this.board = board;
        this.theKingPiece = setKingPiece();
        this.allLegalMoves = ImmutableList.copyOf(Iterables.concat(allLegalMoves, calculateKingCastles(allLegalMoves, allOpposingMoves)));
        this.isChecked = !Player.calculateAttacksOnTile(this.theKingPiece.getPosition(), allOpposingMoves).isEmpty();
    }

    // Find all the enemy moves that are attacking a specific tile coordinate
    // Useful for seeing if a player is in check, checkmate or if there is a stalemate.
    protected static Collection<Move> calculateAttacksOnTile(final int position, final Collection<Move> allOpposingMoves) {
        
        List<Move> attacksOnTile = new ArrayList<>();

        for(final Move move : allOpposingMoves) { // For all moves in the collection of allOpposingMoves
            if(position == move.getDestinationPosition()) { // If the destination position of that move matches the position being checked
                attacksOnTile.add(move); // Add it to the list of attacking moves.
            }
        }
        return ImmutableList.copyOf(attacksOnTile);
    }

    public Collection<Move> getLegalMoves() {
        return this.allLegalMoves;
    }

    // If a move was successfully made, return a BoardTransition object, which will wrap the board state being transitioned to
    public BoardTransition makeMove(final Move move) {
        if(!checkLegalMove(move)) { // If the move isn't legal, no changes to the board will be made
            System.out.println("This move is illegal.");
            return new BoardTransition(this.board, move, MoveStatus.ILLEGAL);
        }

        final Board transitioningBoard = move.execute(); // Polimorphically execute the move

        /* Explanation for the attacksOnKing (IMPORTANT!)
        This addresses the case where a player makes a move that leaves their King vulnerable.
        In chess, a player cannot make a move that leaves them in check, since they would just automatically lose.
        However, calculateLegalMoves does not account for whether or not the moves cause the player to be in check, so we need to manually check for this now.
        To account for this case, I can use the calculateAttacksOnTile and evaluate the state of the board AFTER the move is made (aka the transitioningBoard).
        Since we executed the move, the current player is actually the opposing player before the move was made (opposing player in the previous turn).
        We are trying to stop the move before it actually happens (i.e. before we are done transitioning to the new board state)
        As such, we take the current players opposing king position (which is actually the former player who made the move)
        And we check if the current player (who was the opposing player when the move was made) has any pieces attacking the King's position after the move was made.
        If attacksOnKing happens to be non-empty, then the previous player made an illegal move that placed him in Check, so we do not transition to the new board.
        */
        final Collection<Move> attacksOnKing = Player.calculateAttacksOnTile(transitioningBoard.currentPlayer().getOpposingPlayer().getPlayerKing().getPosition(),
            transitioningBoard.currentPlayer().getLegalMoves());

        if(!attacksOnKing.isEmpty()) {
            return new BoardTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new BoardTransition(transitioningBoard, move, MoveStatus.DONE);
    }

    // Method to find the king piece
    private King setKingPiece() {
        for(final Piece piece : findActivePieces()) {
            if(piece.toString().equals("K")) {
                return (King) piece;
            }
        }
        throw new RuntimeException("There is no King!");
    }

    public King getPlayerKing() {
        return this.theKingPiece;
    }

    // If the player is in check, they must make a move that protects their King
    public boolean isChecked() {
        return this.isChecked;
    }

    // If the player is checkmated, the game is over
    public boolean checkmated() {
        return this.isChecked && !canEscape();
    }

    /* canEscape Method
    The canEscape method is called when a player is in check and to check if there is a stalemate.
    It works by checking every single legal move a player can make and 'simulates' the move by calling the makeMove function.
    If any of the legal moves were successfully completed, then there exists a move that the player can make which can either:
    When in Check:
    - protect the king
    - move the king out to safety
    If no the player has 
    When not in check:
    - The player cannot make any more moves that do not place the King in danger.
    */
    protected boolean canEscape() {
        for(final Move move : this.allLegalMoves) {
            final BoardTransition br = makeMove(move);
            if(br.getMoveStatus().isCompleted()) {
                return true;
            }
        }
        return false;
    }

    // Method to check if the move the player trying to make is a legal move
    public boolean checkLegalMove(final Move move) {
        return this.allLegalMoves.contains(move);
    }

    // If the player is in a stalemate, the game is over
    public boolean stalemated() {
        return !this.isChecked && !canEscape();
    }

    // A player can only castle once per game
    public boolean hasCastled() {
        return false;
    }

    public abstract Collection<Piece> findActivePieces();

    public abstract Type getType();

    public abstract Player getOpposingPlayer();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);

}
