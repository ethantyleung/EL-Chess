package chess.board;

import chess.board.Board.BoardBuilder;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Rook;


// The move class provides the implentation to execute a move when it is made.
// i.e. it will create an entirely new board in the state that the board will be in after a move is executed. (the board is immutable)
public abstract class Move {
    
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destination;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destination){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destination) {
        this.board = board;
        this.destination = destination;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) o;
        return this.getDestinationPosition() == otherMove.getDestinationPosition() && this.getMovedPiece() == otherMove.getMovedPiece()
            && getCurrentPosition() == otherMove.getCurrentPosition();
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + this.movedPiece.getPosition();
        result = 31 * result + movedPiece.hashCode();
        result = 31 * result + this.destination;
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getCurrentPosition() {
        return this.movedPiece.getPosition();
    }

    public int getDestinationPosition() {
        return this.destination;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastling() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {

        final BoardBuilder boardBuilder = new BoardBuilder();

        // Leave all the pieces that are not the movedPiece unchanged.
        for(final Piece piece : board.currentPlayer().findActivePieces()) {
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

    // A non-attacking move to an empty tile.
    public static final class BaseMove extends Move {
        
        public BaseMove(final Board board, final Piece movedPiece, final int destination){
            super(board, movedPiece, destination);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof BaseMove && super.equals(o);
        }

        @Override
        public String toString(){
            if(movedPiece.toString().equals("P")) return Board.getCodeAtPosition(this.destination);
            return movedPiece.toString() + Board.getCodeAtPosition(this.destination);
        }
    }
    
    // An attacking move to an occupied tile.
    public static class AttackMove extends Move {
        
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece) {
            super(board, movedPiece, destination);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(!(o instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherMove = (AttackMove) o;
            return super.equals(otherMove) && this.getAttackedPiece().equals(otherMove.getAttackedPiece());
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return attackedPiece;
        }

        @Override
        public String toString() {
            return this.movedPiece.toString() + "x" + Board.getCodeAtPosition(this.destination);
        }

    }

    // An attacking pawn move
    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece) {
            super(board, movedPiece, destination, attackedPiece);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof PawnAttackMove && super.equals(o);
        }

        @Override
        public String toString() {
            return Board.getCodeAtPosition(this.movedPiece.getPosition()).substring(0,1) + "x" + Board.getCodeAtPosition(this.destination);
        }

    }

    // The special move, en passant
    public static final class EnPassant extends PawnAttackMove {
        public EnPassant(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece) {
            super(board, movedPiece, destination, attackedPiece);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof EnPassant && super.equals(o);
        }

        @Override
        public Board execute() {
            final BoardBuilder boardBuilder = new BoardBuilder();
            for(final Piece piece : this.board.currentPlayer().findActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    boardBuilder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpposingPlayer().findActivePieces()) {
                if(!piece.equals(this.getAttackedPiece())) {
                    boardBuilder.setPiece(piece);
                }
            }
            boardBuilder.setPiece(this.movedPiece.movePiece(this));
            boardBuilder.setMoveMaker(this.board.currentPlayer().getOpposingPlayer().getType());
            return boardBuilder.build();
        }
    }

    // A pawn move
    public static final class PawnMove extends Move {

        public PawnMove(final Board board, final Piece movedPiece, final int destination){
            super(board, movedPiece, destination);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof PawnMove && super.equals(o);
        }

        @Override
        public String toString() {
            return Board.getCodeAtPosition(this.destination);
        }

    }

    // A pawn jump (pawn moving two tiles)
    public static final class PawnJump extends Move {

        public PawnJump(final Board board, final Piece movedPiece, final int destination){
            super(board, movedPiece, destination);
        }

        @Override
        public Board execute() {
            final BoardBuilder boardBuilder = new BoardBuilder();
            for(final Piece piece : this.board.currentPlayer().findActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    boardBuilder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpposingPlayer().findActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    boardBuilder.setPiece(piece);
                }
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            boardBuilder.setPiece(movedPawn);
            boardBuilder.setEnPassantPawn(movedPawn);
            boardBuilder.setMoveMaker(this.board.currentPlayer().getOpposingPlayer().getType());
            return boardBuilder.build();
        }

        @Override
        public String toString() {
            return Board.getCodeAtPosition(this.destination);
        }

    }  

    // Special move type: castling
    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookDestination;
        protected final int castleRookInitialPos;
        
        public CastleMove(final Board board, final Piece movedPiece, final int destination, final Rook castleRook,
                          final int castleRookDestination, final int castleRookInitialPos){
            super(board, movedPiece, destination);
            this.castleRook = castleRook;
            this.castleRookDestination = castleRookDestination;
            this.castleRookInitialPos = castleRookInitialPos;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastling() {
            return true;
        }

        @Override
        public Board execute() {

            final BoardBuilder boardBuilder = new BoardBuilder();

            for(final Piece piece : this.board.currentPlayer().findActivePieces()) {
                if(!this.movedPiece.equals(piece) && piece.getPosition() != this.castleRookInitialPos) {
                    boardBuilder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpposingPlayer().findActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    boardBuilder.setPiece(piece);
                }
            }

            boardBuilder.setPiece(this.movedPiece.movePiece(this));
            boardBuilder.setPiece(new Rook(this.castleRook.getType(), this.castleRookDestination, false));
            boardBuilder.setMoveMaker(this.board.currentPlayer().getOpposingPlayer().getType());
            return boardBuilder.build();
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + this.castleRook.hashCode();
            result = 31 * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(!(o instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherMove = (CastleMove) o;
            return super.equals(otherMove) && this.castleRook.equals(otherMove.getCastleRook());
        }
    }

    // Castling on the kingside
    public static final class KingSideCastle extends CastleMove {

        public KingSideCastle(final Board board, final Piece movedPiece, final int destination, final Rook rook,
                              final int castleRookDestination, final int castleRookInitialPos){
            super(board, movedPiece, destination, rook, castleRookDestination, castleRookInitialPos);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof KingSideCastle && super.equals(o);
        }

        // Convention for king side castle
        @Override
        public String toString() {
            return "O-O";
        }
    }

    // Castling on the queenside
    public static final class QueenSideCastle extends CastleMove {

        public QueenSideCastle(final Board board, final Piece movedPiece, final int destination, final Rook castleRook,
                               final int castleRookDestination, final int castleRookInitialPos){
            super(board, movedPiece, destination, castleRook, castleRookDestination, castleRookInitialPos);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof QueenSideCastle && super.equals(o);
        }

        // Convention for queen side castle
        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    // Invalid move type null move
    public static final class NullMove extends Move {
        public NullMove(){
            super(null,-1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execut a null move.");
        }

    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Cannot instantiate.");
        }

        public static Move createMove(final Board board, final int currentPosition, final int destination) {

            for(final Move move : board.currentPlayer().getLegalMoves()) {
                if(move.getCurrentPosition() == currentPosition && move.getDestinationPosition() == destination) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
