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
import chess.pieces.Rook;

public class BlackPlayer extends Player {
    
    public BlackPlayer(final Board board, final Collection<Move> allBlackMoves, final Collection<Move> allWhiteMoves) {
        super(board, allBlackMoves, allWhiteMoves);
    }

    @Override
    public Collection<Piece> findActivePieces() {
        return board.getBlackPieces();
    }

    @Override
    public Type getType() {
        return Type.BLACK;
    }

    @Override
    public Player getOpposingPlayer() {
        return this.board.getWhitePlayer();
    }

    // Checks if the player can perform a castle and returns a list of moves.
    /* Conditions for castling:
    1. Neither the king nor the rook has previously moved.
    2. There are no pieces between the king and the rook.
    3. The king is not currently in check.
    4. The king does not pass through or finish on a square that is attacked by an enemy piece.
    */
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {

        final List<Move> kingCastles = new ArrayList<>();

        if(this.getPlayerKing().isFirstMove() && !this.isChecked()) { // The king hasn't moved and isn't checked
            // Black King Side Castle
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) { // No tiles blocking the castle
                final Tile rookTile = this.board.getTile(7);
                // Checks if there is a piece at the edge and it is a rook and hasn't moved.
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().toString().equals("R")) {
                    if(Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty()
                    && Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty()) { // Not moving through attacked tiles.
                        kingCastles.add(new Move.KingSideCastle(this.board, this.theKingPiece, 6, (Rook)rookTile.getPiece(),
                                                                5, 7));
                    }
                }
            }
        }

        if(this.getPlayerKing().isFirstMove() && !this.isChecked()) { // The king hasn't moved and isn't checked
            // Black Queen Side Castle
            if(!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                // Checks if there is a piece at the edge and it is a rook and hasn't moved.
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().toString().equals("R")) {
                    if(Player.calculateAttacksOnTile(1, opponentsLegals).isEmpty()
                    && Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty()
                    && Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty()) { // Not moving through attacked tiles.
                        kingCastles.add(new Move.QueenSideCastle(this.board, this.theKingPiece, 2, (Rook)rookTile.getPiece(),
                        3, 0));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }

}
