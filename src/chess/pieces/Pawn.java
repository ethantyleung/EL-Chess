package chess.pieces;

/* Start of package imports */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.common.collect.ImmutableList;
import chess.Type;
import chess.board.*;
import chess.board.Move.BaseMove;
import chess.board.Move.PawnAttackMove;
/* End of package imports*/
import chess.board.Move.PawnJump;

/* The Pawn subclass. Describes the Pawn piece in Chess.
*
*/
public class Pawn extends Piece {
    
    // Pawns can only move forward (offset by 8), or attack diagonally. The attacks will be considered in a seperate case.
    private final static int POSSIBLE_DIRECTION = 8;

    // Arbitrary value to organize pieces in move log
    private final static int PAWN_VALUE = 0;

    public Pawn(final Type pieceType, final int position) {
        super(pieceType, position, true, PAWN_VALUE);
    }

    public Pawn(final Type pieceType, final int position, final boolean firstMove) {
        super(pieceType, position, firstMove, PAWN_VALUE);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        int possibleDestinationPosition = this.position;

        final int offset = POSSIBLE_DIRECTION * this.getType().getDirection(); // Accounts for directional change based on piece type
        possibleDestinationPosition += offset; // Apply the offset (one tile forward)

        if(Board.isValid(possibleDestinationPosition)) {
            // Checks if the pawn can move to the current possibleDestinationPosition
            if(!board.getTile(possibleDestinationPosition).isTileOccupied()) {
                legalMoves.add(new BaseMove(board, this, possibleDestinationPosition));
                // In order for a Pawn to jump a tile, it must be its first move, and both tiles in front must be empty.
                // It would be redundant to check if it is a valid tile (if it is a pawn's first move, it can only move to valid tiles on the board).
                if(firstMove && !board.getTile(possibleDestinationPosition + offset).isTileOccupied()) {
                    // DEBUG: System.out.println("Pawns can jump.");
                    legalMoves.add(new PawnJump(board, this, possibleDestinationPosition + offset));
                }

            }

            // Checks if the pawn can attack (it can if either diagonal tile contains an opposing piece)
            // Check LEFT
            if(board.getTile(this.position + offset - 1).isTileOccupied()){
                final Tile possibleDestinationTile = board.getTile(this.position + offset - 1);
                if( (this.position) % 8 != 0) { // Edge case - first column
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                    final Type typeAtDestination = pieceAtDestination.getType();
                    if(this.getType() != typeAtDestination){
                        legalMoves.add(new PawnAttackMove(board, this, possibleDestinationTile.getTileCoordinate(), pieceAtDestination));
                    }
                }
            }

            // Check RIGHT
            if(board.getTile(this.position + offset + 1).isTileOccupied()){
                final Tile possibleDestinationTile = board.getTile(this.position + offset + 1);
                if( (this.position) % 8 != 7) { // Edge case - last column
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();
                    final Type typeAtDestination = pieceAtDestination.getType();
                    if(this.getType() != typeAtDestination){
                        legalMoves.add(new PawnAttackMove(board, this, possibleDestinationTile.getTileCoordinate(), pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().getType(), move.getDestinationPosition(), false);
    }

    @Override
    public String toString() {
        return "P";
    }
}
