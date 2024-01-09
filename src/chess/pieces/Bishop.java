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
    private Bishop(final int position, final Type pieceType){
        super(position, pieceType);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(int i = 0; i < POSSIBLE_DIRECTIONS.length; i++) { // Iterate through each diagonal direction.
            
            int possiblePosition = this.position;
            int offset = POSSIBLE_DIRECTIONS[i];

            while(Board.isValid(possiblePosition)){ // Run while the current tile is still on the board.

                possiblePosition += offset; // Apply the offset.

                if(Board.isValid(possiblePosition)){ // Check if the tile is still valid.

                    final Tile possibleDestination = board.getTile(possiblePosition);

                    if(!possibleDestination.isTileOccupied()){
                        legalMoves.add(new BaseMove(board, this, possiblePosition));
                    } else {
                        final Piece pieceAtDestination = possibleDestination.getPiece();
                        final Type type = pieceAtDestination.getType();
                        if(this.pieceType != type) legalMoves.add(new AttackMove(board, pieceAtDestination, i, pieceAtDestination));
                        // Since the tile is occupied, there is a piece blocking further potential moves from being made in this direction. Thus, break.
                        break;
                    }
                }
            }            
        }
        return ImmutableList.copyOf(legalMoves);
    }

}
