package chess;

import chess.board.Board;
import chess.gui.Game;

public class EL_Chess {
    
    public static void main(String[] args) {
        
        Board board = Board.createStandardBoard();

        System.out.println(board);

        Game game = new Game();
    }

}