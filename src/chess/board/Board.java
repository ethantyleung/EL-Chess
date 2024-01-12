package chess.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import chess.Type;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class Board {

    public static final int NUM_TILES = 64;
    private final List<Tile> gameBoard; // Use a list for immutability (an array cannot be made immutable and I do not want the board to be changed)

    private Board(BoardBuilder builder) {
        this.gameBoard = createGameBoard(builder);
    }

    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }
    
    public static boolean isValid(int coordinate){
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    // Reads mapped pieces off of the Map initialized by the builder class and transfers them into a List.
    private static List<Tile> createGameBoard(final BoardBuilder builder) {
        final Tile[] tiles = new Tile[NUM_TILES];
        for(int i = 0; i < NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
    final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Rook(Type.BLACK, 0));
        builder.setPiece(new Knight(Type.BLACK, 1));
        builder.setPiece(new Bishop(Type.BLACK, 2));
        builder.setPiece(new Queen(Type.BLACK, 3));
        builder.setPiece(new King(Type.BLACK, 4));
        builder.setPiece(new Bishop(Type.BLACK, 5));
        builder.setPiece(new Knight(Type.BLACK, 6));
        builder.setPiece(new Rook(Type.BLACK, 7));
        builder.setPiece(new Pawn(Type.BLACK, 8));
        builder.setPiece(new Pawn(Type.BLACK, 9));
        builder.setPiece(new Pawn(Type.BLACK, 10));
        builder.setPiece(new Pawn(Type.BLACK, 11));
        builder.setPiece(new Pawn(Type.BLACK, 12));
        builder.setPiece(new Pawn(Type.BLACK, 13));
        builder.setPiece(new Pawn(Type.BLACK, 14));
        builder.setPiece(new Pawn(Type.BLACK, 15));
        
        // White Layout
        builder.setPiece(new Pawn(Type.WHITE, 48));
        builder.setPiece(new Pawn(Type.WHITE, 49));
        builder.setPiece(new Pawn(Type.WHITE, 50));
        builder.setPiece(new Pawn(Type.WHITE, 51));
        builder.setPiece(new Pawn(Type.WHITE, 52));
        builder.setPiece(new Pawn(Type.WHITE, 53));
        builder.setPiece(new Pawn(Type.WHITE, 54));
        builder.setPiece(new Pawn(Type.WHITE, 55));
        builder.setPiece(new Rook(Type.WHITE, 56));
        builder.setPiece(new Knight(Type.WHITE, 57));
        builder.setPiece(new Bishop(Type.WHITE, 58));
        builder.setPiece(new Queen(Type.WHITE, 59));
        builder.setPiece(new King(Type.WHITE, 60));
        builder.setPiece(new Bishop(Type.WHITE, 61));
        builder.setPiece(new Knight(Type.WHITE, 62));
        builder.setPiece(new Rook(Type.WHITE, 63));

        // White to move first
        builder.setMoveMaker(Type.WHITE);

        // Return built board
        return builder.build();
    }

    // Using builder design pattern to create our gameboard.
    // The builder maps each piece to a corresponding tile coordinate on the chess board.
    public static class BoardBuilder {

        Map<Integer, Piece> boardConfig; // Key - Specific Tile position, Value - Chess Piece on that Tile
        Type nextMoveMaker;

        public BoardBuilder() {
        }

        public BoardBuilder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPosition(), piece);
            return this;
        }

        public BoardBuilder setMoveMaker(final Type nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

    }

}
