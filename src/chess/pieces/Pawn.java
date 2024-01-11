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

public class Pawn extends Piece {
    
    private final static int[] POSSIBLE_DIRECTIONS = {8};

    Pawn(final int position, final Type pieceType, final boolean firstMove) {
        super(position, pieceType, firstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        int possibleDestinationPosition = this.position;

        for(int i = 0; i < POSSIBLE_DIRECTIONS.length; i++){
            
            final int offset = POSSIBLE_DIRECTIONS[i] * this.getType().getDirection();
            possibleDestinationPosition += offset;

            if(!Board.isValid(possibleDestinationPosition)) continue;

            // Checks if the pawn can move/jump
            if(!board.getTile(possibleDestinationPosition).isTileOccupied()) {
                legalMoves.add(new BaseMove(board, this, possibleDestinationPosition));
                // In order for a Pawn to jump a tile, it must be its first move, and both tiles in front must be empty.
                if(firstMove && !board.getTile(possibleDestinationPosition + offset).isTileOccupied()) {
                    legalMoves.add(new BaseMove(board, this, possibleDestinationPosition + offset));
                }
            }

            // Checks if the pawn can attack (it can if either diagonal tile contains an opposing piece)
            if(board.getTile(this.position + offset - 1).isTileOccupied()){ // Check LEFT
                final Tile possibleDestinationTile = board.getTile(this.position + offset - 1);
                if( (this.position) % 8 != 0) { // Edge case - first column
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                    final Type typeAtDestination = pieceAtDestination.getType();
                    if(this.getType() != typeAtDestination){
                        legalMoves.add(new AttackMove(board, this, possibleDestinationPosition, pieceAtDestination));
                    }
                }
            }
            if(board.getTile(this.position + offset + 1).isTileOccupied()){ // Check RIGHT
                final Tile possibleDestinationTile = board.getTile(this.position + offset + 1);
                if( (this.position) % 8 != 7) { // Edge case - last column
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                    final Type typeAtDestination = pieceAtDestination.getType();
                    if(this.getType() != typeAtDestination){
                        legalMoves.add(new AttackMove(board, this, possibleDestinationPosition, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

}
