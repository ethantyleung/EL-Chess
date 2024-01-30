package chess.board;

/* Start of package imports */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import chess.players.BlackPlayer;
import chess.players.Player;
import chess.players.WhitePlayer;
/* End of package imports*/

public class Board {

    public static final int NUM_TILES = 64;
    private final List<Tile> gameBoard; // Use a list for immutability (an array cannot be made immutable)
    
    private final Collection<Piece> whitePieces;
    private final WhitePlayer whitePlayer;
    private final Collection<Piece> blackPieces;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;

    public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> CODE_TO_POSITION = initializeCodeToPositionMap();

    private Board(final BoardBuilder builder) {
        this.gameBoard = createGameBoard(builder);
        this.enPassantPawn = builder.enPassantPawn;
        this.whitePieces = findActivePieces(this.gameBoard, Type.WHITE);
        this.blackPieces = findActivePieces(this.gameBoard, Type.BLACK);
        final Collection<Move> allWhiteMoves = findAllLegalMoves(this.whitePieces);
        final Collection<Move> allBlackMoves = findAllLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, allWhiteMoves, allBlackMoves);
        this.blackPlayer = new BlackPlayer(this, allBlackMoves, allWhiteMoves);
        this.currentPlayer = builder.nextMoveMaker.chooseNextPlayer(this.blackPlayer,this.whitePlayer);
    }

    // Getter method for all the white pieces
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    // Getter method for all the black pieces
    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    public Player getBlackPlayer() {
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }

    private static Map<String, Integer> initializeCodeToPositionMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    public static int getPositionAtCode(final String code) {
        return CODE_TO_POSITION.get(code);
    }

    public static String getCodeAtPosition(final int position) {
        return ALGEBRAIC_NOTATION.get(position);
    }

    // Finds all the active pieces of a given type and returns a list of pieces still on the board.
    // This will be useful for calculating all the legal moves in the board's current state to check for illegal moves.
    // e.g. King cannot move to an attacked tile.
    private Collection<Piece> findActivePieces(final List<Tile> gameBoard, final Type type) {

        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile : gameBoard) { // For each tile in the gameboard
            if(tile.isTileOccupied()) { // For every occupied tile that matches the type, add it to the list
                final Piece piece = tile.getPiece();
                if(piece.getType() == type) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    // Finds all the legal moves using the collection of active pieces found in the findActivePieces function
    private Collection<Move> findAllLegalMoves(final Collection<Piece> pieces) {

        final List<Move> allMoves = new ArrayList<>();

        for(Piece piece : pieces) { // For each piece in the collection of pieces.
            allMoves.addAll(piece.calculateLegalMoves(this));
        }

        return ImmutableList.copyOf(allMoves);


    }

    // Returns a Tile object at a specified coordinate
    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }
    
    // Checks if the given coordinate is a valid coordinate (i.e. between 0-63)
    public static boolean isValid(final int coordinate){
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
        // Set all the black pieces in the default chess positions
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
        
        // Set all the white pieces in the default chess positions (white side)
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
    
    // Overrided toString method to print out the board.
    @Override
    public String toString() {
        final StringBuilder boardOutput = new StringBuilder();
        for(int i = 0; i < NUM_TILES; i++) {
            final String tileOutput = this.gameBoard.get(i).toString();
            boardOutput.append(String.format("%3s",tileOutput)); // Format the string so that a minimum of 3 characters are added (for nice spacing), if the string is not three characters long, add spaces in front until it is
            if( (i+1) % 8 == 0) boardOutput.append("\n");
        }
        return boardOutput.toString();
    }

    // Using builder design pattern to create our gameboard.
    // The builder maps each piece to a corresponding tile coordinate on the chess board.
    public static class BoardBuilder {

        Map<Integer, Piece> boardConfig; // Key - Specific Tile position, Value - Chess Piece on that Tile
        Type nextMoveMaker;
        Pawn enPassantPawn;
        Move transitionMove;

        public BoardBuilder() {
            boardConfig = new HashMap<>();
        }

        // Function to set pieces on the game board
        public BoardBuilder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPosition(), piece);
            return this; // Return pointer to the current object being built.
        }

        public BoardBuilder setMoveMaker(final Type nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

        // Keeping track of an enpassant pawn is quite convenient as enpassant availability can be checked by simply finding
        // any adjacent pawns tagged as an enpassant pawn!
        public void setEnPassantPawn(final Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        public BoardBuilder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }
    }
}
