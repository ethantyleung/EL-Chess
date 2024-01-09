package chess.pieces;

/* Start of package imports */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.common.collect.ImmutableList;
import chess.Type;
import chess.board.*;
/* End of package imports*/

/* The Knight subclass. Describes the Knight piece in Chess.
*
*/
public class Knight extends Piece {

    private final static int[] POSSIBLE_MOVES = {-17, -15, -10, 6, 6, 10, 15, 17};

    private Knight(final int position, Type pieceType){
        super(position, pieceType);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        int possiblePosition;

        for(int i = 0; i < POSSIBLE_MOVES.length; i++){
            possiblePosition = this.position + POSSIBLE_MOVES[i];
            if(Board.isValid(possiblePosition)) {
                // Knights can only move a maximum of two columns, consider edge cases
                if(validKnightMove(this.position, possiblePosition)) continue;

                final Tile possibleDestination = board.getTile(this.position + POSSIBLE_MOVES[i]);

                if(!possibleDestination.isTileOccupied()) { // If the tile is not occupied
                    legalMoves.add(new Move()); // Non-attacking move.
                } else {
                    final Piece pieceAtDestination = possibleDestination.getPiece();
                    final Type type = pieceAtDestination.getType();
                    if(this.pieceType != type) legalMoves.add(new Move()); // Attacking move.
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
    
    private boolean validKnightMove(final int coordinate, final int possiblePosition){
        // Taking the mod of a linear position value returns the column - 1. 
        // Checks if the difference between the two columns is greater than two.
        return ( Math.abs( (coordinate % 8) - (possiblePosition % 8)) > 2);
    }

}