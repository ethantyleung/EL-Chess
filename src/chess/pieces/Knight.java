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


/* The Knight subclass. Describes the Knight piece in Chess.
*
*/
public class Knight extends Piece {

    // Possible Moves array contains offsets required to move Knight in every possible direction.
    private final static int[] POSSIBLE_MOVES = {-17, -15, -10, 6, 6, 10, 15, 17};

    // Base Knight Constructor
    public Knight(final Type pieceType, final int position){
        super(pieceType, position, true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        int possibleDestinationPosition;

        for(int i = 0; i < POSSIBLE_MOVES.length; i++){
            possibleDestinationPosition = this.position + POSSIBLE_MOVES[i]; // Apply offset
            if(Board.isValid(possibleDestinationPosition)) {
                // Knights can only move a maximum of two columns, consider edge cases
                if(notValidKnightMove(this.position, possibleDestinationPosition)) continue;

                final Tile possibleDestinationTile = board.getTile(this.position + POSSIBLE_MOVES[i]);

                if(!possibleDestinationTile.isTileOccupied()) { // If the tile is not occupied
                    legalMoves.add(new BaseMove(board, this, possibleDestinationPosition)); // Add a Non-attacking move.
                } else {
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                    final Type typeAtDestination = pieceAtDestination.getType();
                    if(this.pieceType != typeAtDestination) legalMoves.add(new AttackMove(board, this, possibleDestinationPosition, pieceAtDestination)); // Add an Attacking move.
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    private boolean notValidKnightMove(final int coordinate, final int possibleDestinationPosition){
        // Taking the mod of a linear position value returns the column - 1. 
        // Checks if the difference between the two columns is greater than two.
        return ( Math.abs( (coordinate % 8) - (possibleDestinationPosition % 8)) > 2);
    }

}