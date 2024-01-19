package chess.players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.Type;
import chess.board.Board;
import chess.board.Move;
import chess.board.Tile;
import chess.pieces.Piece;

public class WhitePlayer extends Player {
    
    public WhitePlayer(final Board board, final Collection<Move> allWhiteMoves, final Collection<Move> allBlackMoves) {
        super(board, allWhiteMoves, allBlackMoves);
    }

    @Override
    public Collection<Piece> findActivePieces() {
        return board.getWhitePieces();
    }

    @Override
    public Type getType() {
        return Type.WHITE;
    }

    @Override
    public Player getOpposingPlayer() {
        return this.board.getBlackPlayer();
    }
    
    // Checks if the player can perform a castle and returns a list of moves.
    /* Conditions for castling:
    1. Neither the king nor the rook has previously moved.
    2. There are no pieces between the king and the rook.
    3. The king is not currently in check.
    4. The king does not pass through or finish on a square that is attacked by an enemy piece.
    */
    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {

        final List<Move> kingCastles = new ArrayList<>();

        if(this.getPlayerKing().isFirstMove() && !this.isChecked()) { // The king hasn't moved and isn't checked
            // White King Side Castle
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) { // No tiles blocking the castle
                final Tile rookTile = this.board.getTile(63);
                // Checks if there is a piece at the edge and it is a rook and hasn't moved.
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().toString().equals("R")) {
                    if(Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty()
                    && Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()) { // Not moving through attacked tiles.
                        // TODO Implement later
                        kingCastles.add(null);
                    }
                }
            }
        }

        if(this.getPlayerKing().isFirstMove() && !this.isChecked()) { // The king hasn't moved and isn't checked
            // White Queen Side Castle
            if(!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                // Checks if there is a piece at the edge and it is a rook and hasn't moved.
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().toString().equals("R")) {
                    if(Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()
                    && Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty()
                    && Player.calculateAttacksOnTile(57, opponentsLegals).isEmpty()) { // Not moving through attacked tiles.
                        // TODO Implement later
                        kingCastles.add(null);
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
