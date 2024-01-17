package chess;

import chess.players.BlackPlayer;
import chess.players.Player;
import chess.players.WhitePlayer;

/* The Type enumerator class. Contains WHITE/BLACK type to describe each piece.
*
*/
public enum Type {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player chooseNextPlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player chooseNextPlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer) {
            return blackPlayer;
        }
    };

    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract Player chooseNextPlayer(BlackPlayer blackPlayer, WhitePlayer whitePlayer);
}
