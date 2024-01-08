/* Package declarations */
package chess.board;
/* End of package declarations */

/* Start of package imports */
import chess.pieces.*;
import java.util.Map;
import java.util.HashMap;
/* End of package imports*/

/* The tile class to describe individual tiles in the board
*
*/
public abstract class Tile {
	
	protected final int TILE_COORDINATE;

	private static final Map<Integer, EmptyTile> EMPTY_TILES = createEmptyTiles();
	
	Tile(int tileCoordinate) {
		this.TILE_COORDINATE = tileCoordinate;
	}
	
	private static Map<Integer, EmptyTile> createEmptyTiles() {

		final Map<Integer, EmptyTile> EMPTY_TILE_MAP = new HashMap<>();

		for(int i = 0; i < 64; i++){
			EMPTY_TILE_MAP.put(i, new EmptyTile(i));
		}

		return EMPTY_TILE_MAP;
	}

	public abstract boolean isTileOccupied();
	
	public abstract Piece getPiece();
	
	public static final class EmptyTile extends Tile{
		
		EmptyTile(final int coordinate) {
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
		
	}
	
	public static final class OccupiedTile extends Tile {
		
		private final Piece pieceOnTile;
		
		OccupiedTile( final int tileCoordinate, final Piece pieceOnTile) {
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
		
	}
	
}
