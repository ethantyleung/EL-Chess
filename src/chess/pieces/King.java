package chess.pieces;

/* Start of package imports */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.common.collect.ImmutableList;
import chess.Type;
import chess.board.*;
import chess.board.Move.AttackMove;
import chess.board.Move.BaseMove;
/* End of package imports*/

/* The King subclass. Describes the King piece in Chess.
*
*/
public class King extends Piece {

    // Possible direction array that contains the required offset for a one tile move in each respective diagonal direction.
    private static final int[] POSSIBLE_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9};

    // Arbitrary value to organize pieces in move log
    private final static int KING_VALUE = 3;
    
    // Base King Constructor
    public King(final Type pieceType, final int position) {
        super(pieceType, position, true, KING_VALUE);
    }

    // King constructor with specified firstMove status
    public King(final Type pieceType, final int position, final boolean firstMove) {
        super(pieceType, position, firstMove, KING_VALUE);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();

        for(int i = 0; i < POSSIBLE_MOVES.length; i++) { // Iterate through each move.
            
            int possibleDestinationPosition = this.position;
            final int OFFSET = POSSIBLE_MOVES[i];

            if(!validKingDirection(OFFSET, possibleDestinationPosition)) continue; // The offset does not work for certain edge cases.

            possibleDestinationPosition += OFFSET; // Apply the offset.

            if(Board.isValid(possibleDestinationPosition)){ // Check if the tile is still valid.

                final Tile possibleDestination = board.getTile(possibleDestinationPosition);

                if(!possibleDestination.isTileOccupied()){
                        legalMoves.add(new BaseMove(board, this, possibleDestinationPosition));
                } else {
                    final Piece pieceAtDestination = possibleDestination.getPiece();
                    final Type typeAtDestination = pieceAtDestination.getType();
                    if(this.pieceType != typeAtDestination) {
                        legalMoves.add(new AttackMove(board, pieceAtDestination, possibleDestinationPosition, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(final Move move) {
        return new King(move.getMovedPiece().getType(), move.getDestinationPosition(), false);
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean validKingDirection(final int direction, final int possibleDestinationPosition){
        boolean valid = true;
        if(possibleDestinationPosition % 8 == 0) { // If the king is in the first column AND
            if(direction == 7 || direction == -9) valid = false; // If the king is trying to move in the bottom left diagonal or top left diagonal, it is not a valid direction.
        }
        else if(possibleDestinationPosition % 8 == 7) { //If the king is in the last column (8th)
            if(direction == -7 || direction == 9) valid = false; // If the king is trying to move in the bottom right diagonal or top right diagonal, it is not a valid direction.
        }
        else if(possibleDestinationPosition % 8 == 0) { // If the king is in the first column AND
            if(direction == -1) valid = false; // If the king is trying to move in the left direction, it is not valid.
        }
        else if(possibleDestinationPosition % 8 == 7) { //If the king is in the last column (8th)
            if(direction == 1) valid = false; // If the king is trying to move in the right direction, it is not valid.
        }
        return valid;
    }
}
