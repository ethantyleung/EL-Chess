package chess.board;

import chess.pieces.Piece;

public abstract class Move {
    
    final Board board;
    final Piece movedPiece;
    final int destination;

    private Move(final Board board, final Piece movedPiece, final int destination){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
    }

    public int getDestinationPosition() {
        return this.destination;
    }

    public abstract Board execute();

    public static final class BaseMove extends Move {
        
        public BaseMove(final Board board, final Piece movedPiece, final int destination){
            super(board, movedPiece, destination);
        }

        @Override
        public Board execute() {
            return null;
        }

    }
    
    public static final class AttackMove extends Move {
        
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece) {
            super(board, movedPiece, destination);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }


    }

}
