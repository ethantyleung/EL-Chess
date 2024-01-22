package chess.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.google.common.collect.Lists;

import chess.board.Board;
import chess.board.Move;
import chess.board.Tile;
import chess.board.Move.AttackMove;
import chess.board.Move.MoveFactory;
import chess.pieces.Piece;
import chess.players.BoardTransition;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static javax.swing.SwingUtilities.*;

public class Game {
    
    // Predefined dimensions for each frame & panel.
    private final static Dimension MAIN_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);

    // The directory that contains the icons
    private static String pieceIconPath = "art/";

    // Predefined color codes for the tiles on the board.
    private final Color lightTileColor = Color.decode("#DBA44F");
    private final Color darkTileColor = Color.decode("#332413");
    
    // Init J-elements
    private final JFrame mainFrame;
    private final JMenuBar menuBar;
    private final BoardPanel boardPanel;

    // Player Control Elements
    private Board chessboard;
    private Tile sourceTile;
    private Tile finalTile;
    private Piece movedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;

    public Game() {
        // Configure the main fame
        this.mainFrame = new JFrame("ELChess");
        this.mainFrame.setLayout(new BorderLayout());
        this.mainFrame.setSize(MAIN_FRAME_DIMENSION);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setLocationRelativeTo(null);

        // Build the menu bar
        this.menuBar = new JMenuBar();
        createMenuBar(menuBar);
        this.mainFrame.setJMenuBar(this.menuBar);
        this.highlightLegalMoves = false;

        // Create standard board
        chessboard = Board.createStandardBoard();

        // Build the board
        this.boardPanel = new BoardPanel();
        this.mainFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.boardDirection = BoardDirection.DEFAULT;

        this.mainFrame.setVisible(true);
    }

    private void createMenuBar(final JMenuBar menuBar) {
        menuBar.add(createFileMenu());
        menuBar.add(createPreferencesMenu());
    }

    // Allow users to load previous games
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening PGN File...");
            }
        });
        fileMenu.add(openPGN);

        // Add another dropdown option, EXIT
        final JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenu);
        return fileMenu;
    }

    // Another menu bar that contains more features
    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");

        // A flip board item that lets users flip the board
        final JMenuItem flipBoardItem = new JMenuItem("Flip Board");
        flipBoardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessboard);
            }
        });
        preferencesMenu.add(flipBoardItem);

        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHightlighterCheckbox = new JCheckBoxMenuItem("Highlight Moves", false);

        legalMoveHightlighterCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHightlighterCheckbox.isSelected();
            }
            
        });

        preferencesMenu.add(legalMoveHightlighterCheckbox);

        return preferencesMenu;
    }

    // Enumerator class that describes the orientation of the board currently displayed
    public enum BoardDirection {
        DEFAULT {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }
            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }
            @Override
            BoardDirection opposite() {
                return DEFAULT;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

    // MoveLog class to keep track of a history of moves
    public static class MoveLog {
        private final List<Move> moveHistory;

        MoveLog() {
            this.moveHistory = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moveHistory;
        }

        public void addMove(final Move move) {
            this.moveHistory.add(move);
        }

        public int size() {
            return this.moveHistory.size();
        }

        public void clear() {
            this.moveHistory.clear();
        }

        public Move removeMove(final int index) {
            return this.moveHistory.remove(index);
        }

        public boolean removeMove(final Move move) {
            return this.moveHistory.remove(move);
        }
    }

    // JPanel that represents the main game board
    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < Board.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        // Update the new state of the tilePanels in boardPanel whenever a move is made
        public void drawBoard(final Board board) {
            this.removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    // JPanels that represent the tiles on the board.
    private class TilePanel extends JPanel {

        private final int tilePosition;

        TilePanel(final BoardPanel boardPanel, final int tilePosition) {
            super(new GridBagLayout());
            this.tilePosition = tilePosition;
            setPreferredSize(TILE_PANEL_DIMENSION);
            setTileColour();
            setTileIcon(chessboard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)) { // Right clicks reset user actions
                        resetState();
                    } else if (isLeftMouseButton(e)) { // Left clicks to initiate moves
                        if(sourceTile == null) { // If the user has not currently selected a tile, set the source tile and piece being moved
                            sourceTile = chessboard.getTile(tilePosition);
                            movedPiece = sourceTile.getPiece();
                            if(movedPiece == null) { // Checks if there was actually a piece at the tile selected
                                sourceTile = null; // If not, then don't set the source tile to anything (the player clicked on an empty tile)
                            }
                        } else { // If the user is already hovering on a piece and selected a new destination, try to execute the move.
                            finalTile = chessboard.getTile(tilePosition);
                            final Move move = Move.MoveFactory.createMove(chessboard, sourceTile.getTileCoordinate(), finalTile.getTileCoordinate());
                            final BoardTransition transition = chessboard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isCompleted()) {
                                chessboard = transition.getTransitioningBoard();
                                // TODO Create a move log by adding the executed move to a list
                            }
                            resetState();
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessboard);
                            }
                        });
                    }
                }
                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            validate();
        }

        public void drawTile(final Board board) {
            setTileColour();
            setTileIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        // Set the color of the tiles according to a normal chess board (I used brown/light brown)
        private void setTileColour() {
            boolean isLight = ((this.tilePosition + this.tilePosition/8) % 2 == 0);
            setBackground(isLight ? lightTileColor : darkTileColor);
        }

        // Placing images of pieces onto tiles
        private void setTileIcon(final Board board) {
            this.removeAll();
            if(board.getTile(this.tilePosition).isTileOccupied()) {
                try {
                    // Naming convention: _ _, first letter is color (W, B), second letter is type of piece.
                    final BufferedImage image = 
                            ImageIO.read(new File(pieceIconPath
                            + board.getTile(this.tilePosition).getPiece().getType().toString().substring(0,1)
                            + board.getTile(this.tilePosition).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Highlight the legal moves
        private void highlightLegalMoves(final Board board) {
            if(highlightLegalMoves) {
                for(final Move move : pieceLegalMoves(board)) { // For every move that is in the piece's set of legal moves
                    if(move.getDestinationPosition() == this.tilePosition && !(move instanceof AttackMove)) { // Check if the move's destination position is the current tile
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/Misc/blackdot.png"))))); // try to add a circle to the tile
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // Calculate a specific piece's legal moves, used to highlight possible moves
        private Collection<Move> pieceLegalMoves(final Board board) {
            if(movedPiece != null && movedPiece.getType() == board.currentPlayer().getType()) {
                return movedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void resetState() {
            sourceTile = null;
            movedPiece = null;
            finalTile = null;
        }
    }
}