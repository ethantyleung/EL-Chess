package chess.board;

import chess.board.Board.BoardBuilder;
import chess.pieces.Piece;

// The move class provides the implentation to execute a move when it is made.
// i.e. it will create an entirely new board in the state that the board will be in after a move is executed. (the board is immutable)
public abstract class Move {
    
    final Board board;
    final Piece movedPiece;
    final int destination;

    private Move(final Board board, final Piece movedPiece, final int destination){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getDestinationPosition() {
        return this.destination;
    }

    public abstract Board execute();

    // A non-attacking move to an empty tile.
    public static final class BaseMove extends Move {
        
        public BaseMove(final Board board, final Piece movedPiece, final int destination){
            super(board, movedPiece, destination);
        }

        @Override
        public Board execute() {

            final BoardBuilder boardBuilder = new BoardBuilder();

            // Leave all the pieces that are not the movedPiece unchanged.
            for(final Piece piece : board.currentPlayer().findActivePieces()) {
                // TODO override hashcode and equals method
                if(!this.movedPiece.equals(piece)) {
                    boardBuilder.setPiece(piece);
                }
            }
            for(final Piece piece : board.currentPlayer().getOpposingPlayer().findActivePieces()) {
                boardBuilder.setPiece(piece);
            }

            boardBuilder.setPiece(this.movedPiece.movePiece(this)); // Setting the moved piece in the new location
            boardBuilder.setMoveMaker(board.currentPlayer().getOpposingPlayer().getType()); // set the next move maker

            return boardBuilder.build();
        }

    }
    
    // An attacking move to an occupied tile.
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
