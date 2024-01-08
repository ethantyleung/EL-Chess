/* Start of Package declarations */
package chess.pieces;
/* End of package declarations */

/* Start of package imports */
import java.util.List;
import chess.Type;
import chess.board.*;
/* End of package imports*/

/* The piece class to describe each type of piece in the Chess game.
*
*/
public abstract class Piece {

    protected final int position;
    protected final Type pieceType;

    private Piece(final int position, Type pieceType){
        this.position = position;
        this.pieceType = pieceType;
    }

    public abstract List<Move> calculateLegalMoves(final Board board);
    
}
