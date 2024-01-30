package chess.board;

/* Start of package imports */
import chess.pieces.*;
import java.util.Map;
import java.util.HashMap;
import com.google.common.collect.ImmutableMap;
/* End of package imports*/

/* The tile class to describe individual tiles in the board
*
*/
public abstract class Tile {
	
	protected final int TILE_COORDINATE; // Every tile has a unique coordinate (0-63).

	// A map with a set of 64 empty tiles mapped to each tile on the board.
	// Useful for filling tiles that are unoccupied by a piece (mirrors the actual board).
	private static final Map<Integer, EmptyTile> EMPTY_TILES = createEmptyTiles();
	
	//Tile Constructor
	private Tile(final int tileCoordinate) {
		this.TILE_COORDINATE = tileCoordinate;
	}
	
	// Creates a tile to add to the game board (either an occupied tile or empty tile depending on the value of piece)
	public static Tile createTile(final int tileCoordinate, final Piece piece){
		if(piece != null) {
			return new OccupiedTile(tileCoordinate, piece);
		} else {
			return EMPTY_TILES.get(tileCoordinate);
		}
	}

	public int getTileCoordinate() {
		return this.TILE_COORDINATE;
	}


	// Fills the EMPTY_TILES map with empty tiles.
	private static Map<Integer, EmptyTile> createEmptyTiles() {
		
		final Map<Integer, EmptyTile> EMPTY_MAP = new HashMap<>();

		for(int i = 0; i < Board.NUM_TILES; i++){
			EMPTY_MAP.put(i, new EmptyTile(i));
		}

		return ImmutableMap.copyOf(EMPTY_MAP);
	}

	public abstract boolean isTileOccupied(); // It will be useful to know which tiles are occupied
	
	public abstract Piece getPiece(); // It will be useful to know what piece is occupying the tile
	
	public static final class EmptyTile extends Tile{
		
		private EmptyTile(final int coordinate) {
			super(coordinate);
		}
		
		@Override
		public boolean isTileOccupied() {
			return false;
		}
		
		@Override
		public Piece getPiece() {
			return null;
		}
		
		@Override
		public String toString() {
			return " - "; // Empty tiles are represented as dashes in the terminal
		}
		
	}
	
	public static final class OccupiedTile extends Tile {
		
		private final Piece pieceOnTile;
		
		private OccupiedTile( final int tileCoordinate, final Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}
		
		@Override
		public boolean isTileOccupied() {
			return true;
		}
		
		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}

		@Override
		public String toString() {
			if(this.pieceOnTile.getType().isBlack()) {
				return this.pieceOnTile.toString() + "'"; // Identifier to differentiate between black and white pieces
			} else {
				return this.pieceOnTile.toString() + "`";
			}
		}
	}
}
