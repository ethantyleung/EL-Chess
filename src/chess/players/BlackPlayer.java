package chess.players;

import java.util.Collection;

import chess.Type;
import chess.board.Board;
import chess.board.Move;
import chess.pieces.Piece;

public class BlackPlayer extends Player {
    
    public BlackPlayer(final Board board, final Collection<Move> allBlackMoves, final Collection<Move> allWhiteMoves) {
        super(board, allBlackMoves, allWhiteMoves);
    }

    @Override
    public Collection<Piece> findActivePieces() {
        return board.getBlackPieces();
    }

    @Override
    public Type getType() {
        return Type.BLACK;
    }

    @Override
    public Player getOpposingPlayer() {
        return this.board.getWhitePlayer();
    }
}
