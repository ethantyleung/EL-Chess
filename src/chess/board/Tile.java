/* Package declarations */
package chess.board;
/* End of package declarations */

/* Start of package imports */
import chess.pieces.*;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
/* End of package imports*/

/* The tile class to describe individual tiles in the board
*
*/
public abstract class Tile {
	
	protected final int TILE_COORDINATE;

	private static final Map<Integer, EmptyTile> EMPTY_TILES = createEmptyTiles();
	
	private Tile(final int tileCoordinate) {
		this.TILE_COORDINATE = tileCoordinate;
	}
	
	public static Tile createTile(final int tileCoordinate, final Piece piece){
		// if piece != null, create new occupied tile with type of piece at coordinate, else return piece object at coordinate
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate);
	}

	private static Map<Integer, EmptyTile> createEmptyTiles() {

		final Map<Integer, EmptyTile> EMPTY_MAP = new HashMap<>();

		for(int i = 0; i < 64; i++){
			EMPTY_MAP.put(i, new EmptyTile(i));
		}

		return ImmutableMap.copyOf(EMPTY_MAP);
	}

	public abstract boolean isTileOccupied();
	
	public abstract Piece getPiece();
	
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
		
	}
	
}
