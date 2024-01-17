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

    // Piece Constructor.
    protected Piece(final Type pieceType, final int position, final boolean firstMove){
        this.pieceType = pieceType;
        this.position = position;
        this.firstMove = firstMove;
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
