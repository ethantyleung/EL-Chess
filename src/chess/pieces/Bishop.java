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

public class Bishop extends Piece{
    
    // Possible direction array that contains the required offset for a one tile move in each respective diagonal direction.
    private final static int[] POSSIBLE_DIRECTIONS = {-9, -7, 7, 9};

    // Base Bishop Constructor
    private Bishop(final int position, final Type pieceType, final boolean firstMove){
        super(position, pieceType, firstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(int i = 0; i < POSSIBLE_DIRECTIONS.length; i++) { // Iterate through each diagonal direction.
            
            int possibleDestinationPosition = this.position;
            int offset = POSSIBLE_DIRECTIONS[i];

            if(!validBishopDirection(offset, possibleDestinationPosition)) continue; // The offset does not work for certain edge cases.

            while(Board.isValid(possibleDestinationPosition)){ // Run while the current tile is still on the board.

                possibleDestinationPosition += offset; // Apply the offset.

                if(Board.isValid(possibleDestinationPosition)){ // Check if the tile is still valid.

                    final Tile possibleDestination = board.getTile(possibleDestinationPosition);

                    if(!possibleDestination.isTileOccupied()){
                        legalMoves.add(new BaseMove(board, this, possibleDestinationPosition));
                    } else {
                        final Piece pieceAtDestination = possibleDestination.getPiece();
                        final Type typeAtDestination = pieceAtDestination.getType();
                        if(this.pieceType != typeAtDestination) legalMoves.add(new AttackMove(board, pieceAtDestination, possibleDestinationPosition, pieceAtDestination));
                        // Since the tile is occupied, there is a piece blocking further potential moves from being made in this direction. Thus, break.
                        break;
                    }
                    if(!validBishopDirection(offset, possibleDestinationPosition)) break; // check if the loop has reached an invalid tile.
                }
            }            
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private boolean validBishopDirection(final int direction, final int possibleDestinationPosition){
        boolean valid = true;
        if(possibleDestinationPosition % 8 == 0) { // If the bishop is in the first column AND
            if(direction == 7 || direction == -9) valid = false; // If the bishop is trying to move in the bottom left diagonal or top left diagonal, it is not a valid direction.
        }
        else if(possibleDestinationPosition % 8 == 7) { //If the bishop is in the last column (8th)
            if(direction == -7 || direction == 9) valid = false; // If the bishop is trying to move in the bottom right diagonal or top right diagonal, it is not a valid direction.
        }
        return valid;
    }

}
