package chess.players;

public enum MoveStatus {
    DONE {
        @Override
        public boolean isCompleted() {
            // DEBUG: System.out.println("Movestatus: COMPLETED.");
            return true;
        }
    },
    ILLEGAL {
        @Override
        public boolean isCompleted() {
            // DEBUG: System.out.println("Movestatus: ILLEGAL.");
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isCompleted() {
            // DEBUG: System.out.println("Movestatus: LEAVES PLAYER IN CHECK.");
            return false;
        }
    };
    public abstract boolean isCompleted();
}
