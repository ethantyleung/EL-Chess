package chess.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import chess.board.Board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    
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
    private final Board chessboard;

    public Table() {
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

        // Create standard board
        chessboard = Board.createStandardBoard();

        // Build the board
        this.boardPanel = new BoardPanel();
        this.mainFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.mainFrame.setVisible(true);
    }

    private void createMenuBar(final JMenuBar menuBar) {
        menuBar.add(createFileMenu());
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
            validate();
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
    }
}