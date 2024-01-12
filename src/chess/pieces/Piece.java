package chess.pieces;

import java.util.Collection;

import chess.Type;
import chess.board.*;
/* End of package imports*/

/* The piece superclass from which each type of Chess piece will be derived.
*
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

    public Type getType(){
        return this.pieceType;
    }

    public int getPosition(){
        return this.position;
    }

    public boolean isFirstMove() {
        return this.firstMove;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

}
