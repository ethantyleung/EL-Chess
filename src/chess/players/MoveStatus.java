package chess.players;

public enum MoveStatus {
    DONE {
        @Override
        boolean isCompleted() {
            return true;
        }
    },
    ILLEGAL {
        @Override
        boolean isCompleted() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK {
        @Override
        boolean isCompleted() {
            return false;
        }
    };
    abstract boolean isCompleted();
}
