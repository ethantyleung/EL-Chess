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

    // Piece Constructor.
    protected Piece(final int position, final Type pieceType){
        this.position = position;
        this.pieceType = pieceType;
    }

    public Type getType(){
        return this.pieceType;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

}
