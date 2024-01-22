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

/* The Rook subclass. Describes the Rook piece in Chess.
*
*/
public class Rook extends Piece{
    
    private final static int[] POSSIBLE_DIRECTIONS = {-8, -1, 1, 8};

    // Arbitrary value to organize pieces in move log
    private final static int ROOK_VALUE = 3;

    public Rook(final Type pieceType, final int position){
        super(pieceType, position, true, ROOK_VALUE);
    }

    public Rook(final Type pieceType, final int positon, final boolean firstMove) {
        super(pieceType, positon, firstMove, ROOK_VALUE);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        int possibleDestinationPosition;

        for(int i = 0; i < POSSIBLE_DIRECTIONS.length; i++) {

            possibleDestinationPosition = this.position;
            int offset = POSSIBLE_DIRECTIONS[i];

            if(!validRookDirection(offset, possibleDestinationPosition)) continue;

            while(Board.isValid(possibleDestinationPosition + offset)) {
                possibleDestinationPosition += offset;
                final Tile possibleDestinationTile = board.getTile(possibleDestinationPosition);
                if(!possibleDestinationTile.isTileOccupied()){
                    legalMoves.add(new BaseMove(board, this, possibleDestinationPosition));
                } else {
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                    final Type type = pieceAtDestination.getType();
                    if(this.pieceType != type) legalMoves.add(new AttackMove(board, this, possibleDestinationPosition, pieceAtDestination));
                    break;
                }
                if(!validRookDirection(offset, possibleDestinationPosition)) break;
            }
        }
        
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(final Move move) {
        return new Rook(move.getMovedPiece().getType(), move.getDestinationPosition(), false);
    }

    @Override
    public String toString() {
        return "R";
    }

    private boolean validRookDirection(final int direction, final int possibleDestinationPosition){
        boolean valid = true;
        if(possibleDestinationPosition % 8 == 0) { // If the rook is in the first column AND
            if(direction == -1) valid = false; // If the rook is trying to move in the left direction, it is not valid.
        }
        else if(possibleDestinationPosition % 8 == 7) { //If the rook is in the last column (8th)
            if(direction == 1) valid = false; // If the bishop is trying to move in the right direction, it is not valid.
        }
        return valid;
    }
}
