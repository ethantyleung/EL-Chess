package chess.players;

import java.util.Collection;

import chess.Type;
import chess.board.Board;
import chess.board.Move;
import chess.pieces.Piece;

public class WhitePlayer extends Player {
    
    public WhitePlayer(final Board board, final Collection<Move> allWhiteMoves, final Collection<Move> allBlackMoves) {
        super(board, allWhiteMoves, allBlackMoves);
    }

    @Override
    public Collection<Piece> findActivePieces() {
        return board.getWhitePieces();
    }

    @Override
    public Type getType() {
        return Type.WHITE;
    }

    @Override
    public Player getOpposingPlayer() {
        return this.board.getBlackPlayer();
    }
}
