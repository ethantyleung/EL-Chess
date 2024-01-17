package chess.players;

import chess.board.Board;
import chess.board.Move;

// Whenever a player makes a move, an entire new board object is created. 
// This is a helper class to transition from one board state to another whenever a move is made since the 
// prior state of the board must be kept, with only the moved piece being 'updated' in the new board build.
public class BoardTransition {

    private final Board transitioningBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public BoardTransition(final Board transitioningBoard, final Move move, final MoveStatus moveStatus) {
        this.transitioningBoard = transitioningBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

}
