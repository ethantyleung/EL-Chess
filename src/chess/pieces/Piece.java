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
    protected Piece(final int position, final Type pieceType, final boolean firstMove){
        this.position = position;
        this.pieceType = pieceType;
        this.firstMove = false;
    }

    public Type getType(){
        return this.pieceType;
    }

    public boolean getFirstMove() {
        return this.firstMove;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

}
