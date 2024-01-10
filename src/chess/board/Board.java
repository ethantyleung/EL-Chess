package chess.board;

public class Board {

    public static final int NUM_TILES = 64;

    public Tile getTile(final int tileCoordinate){
        return null;
    }
    
    public static boolean isValid(int coordinate){
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

}
