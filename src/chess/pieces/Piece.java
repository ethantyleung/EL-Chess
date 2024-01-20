package chess.pieces;

import java.util.Collection;

import chess.Type;
import chess.board.*;
/* End of package imports*/

/* The piece superclass from which each type of Chess piece will be derived.
Attributes:
- A position (or coordinate) on the board
- A type (white or black)
- A boolean identifier to check if it has moved (for castling and pawn jumps)
*/
public abstract class Piece {

    protected final int position;
    protected final Type pieceType;
    protected final boolean firstMove;
    private final int cachedHashCode;

    // Piece Constructor.
    protected Piece(final Type pieceType, final int position, final boolean firstMove){
        this.pieceType = pieceType;
        this.position = position;
        this.firstMove = firstMove;
        this.cachedHashCode = computeHashCode();
    }

    // Overriding the default equals method to check equality of two Piece objects by attributes instead of reference.
    @Override
    public boolean equals(final Object o) {
        if(this == o) { // If they are referring to the same object, then return true
            return true;
        }
        if(!(o instanceof Piece)) { // If o is not an instance of piece, it can't be equal
            return false;
        }
        final Piece otherPiece = (Piece) o;
        // If the type, position, firstmove and piece-type of the two objects match, they are equal.
        return this.getType() == otherPiece.getType() && this.getPosition() == otherPiece.getPosition()
               && this.isFirstMove() == otherPiece.isFirstMove() && this.toString().equals(otherPiece.toString());
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    // Implementation of the overrided hashCode (the equals method was overrided so we must also override the hashCode method) 
    private int computeHashCode() {
        int result = 17;
        result = 31 * result + pieceType.hashCode();
        result = 31 * result + position;
        result = 31 * result + (firstMove ? 1 : 0);
        return result;
    }

    // Getter method for pieceType
    public Type getType(){
        return this.pieceType;
    }

    // Getter method for position of the piece
    public int getPosition(){
        return this.position;
    }

    // Getter method to check if a piece has moved before
    public boolean isFirstMove() {
        return this.firstMove;
    }

    // Move a piece and return the piece with the updated position
    public abstract Piece movePiece(final Move move); 

    // Every piece will have a set of legal moves, which will be implemented differently depending on the piece.
    // Param - takes in the current state of the gameboard
    public abstract Collection<Move> calculateLegalMoves(final Board board);

}
