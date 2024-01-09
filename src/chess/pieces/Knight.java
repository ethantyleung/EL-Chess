package chess.pieces;

/* Start of package imports */
import java.util.ArrayList;
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
    public List<Move> calculateLegalMoves(Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();

        for(int i = 0; i < POSSIBLE_MOVES.length; i++){
            if(this.position + POSSIBLE_MOVES[i] >= 0 && this.position + POSSIBLE_MOVES[i] <= 63) {
                
                final Tile possibleDestination = board.getTile(this.position + POSSIBLE_MOVES[i]);

                if(!possibleDestination.isTileOccupied()) { // If the tile is not occupied
                    legalMoves.add(new Move());
                } else {
                    final Piece pieceAtDestination = possibleDestination.getPiece();
                    final Type type = pieceAtDestination.getType();
                    if(this.pieceType != type) legalMoves.add(new Move());
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
    
}