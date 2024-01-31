package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import chess.board.Move;
import chess.gui.Game.MoveLog;
import chess.pieces.Piece;

public class CapturedPiecesSideBar extends JPanel{
    
    // Predefined dimensions for each frame & panel.
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Dimension DIMENSION_OF_PIECES = new Dimension(40,30);

    // Init J-elements
    private final JPanel northPanel;
    private final JPanel southPanel;

    public CapturedPiecesSideBar() {
        super(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(15, 1));
        this.southPanel = new JPanel(new GridLayout(15, 1));
        this.northPanel.setBackground(Color.LIGHT_GRAY);
        this.southPanel.setBackground(Color.LIGHT_GRAY);
        add(this.southPanel, BorderLayout.SOUTH);
        add(this.northPanel, BorderLayout.NORTH);
        setPreferredSize(DIMENSION_OF_PIECES);
    }

    public void logReset(final MoveLog moveLog) {

        // Clear out the logs
        this.northPanel.removeAll();
        this.southPanel.removeAll();

        // Two array lists to hold the capture white and black pieces respectively
        final List<Piece> capturedWhitePieces = new ArrayList<>();
        final List<Piece> capturedBlackPieces = new ArrayList<>();

        // Iterate through the move log and update the array lists
        for(final Move move : moveLog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getType().isWhite()) {
                    capturedWhitePieces.add(takenPiece);
                } else if(takenPiece.getType().isBlack()){
                    capturedBlackPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here!");
                }
            }
        }

        for(int i = 0; i < capturedBlackPieces.size(); i++) {
            final Piece takenPiece = capturedBlackPieces.get(i);
            try{
                final BufferedImage image = ImageIO.read(new File("art/" + takenPiece.getType().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 50, icon.getIconWidth() - 50, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel,i);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < capturedWhitePieces.size(); i++) {
            final Piece takenPiece = capturedWhitePieces.get(i);
            try{
                final BufferedImage image = ImageIO.read(new File("art/" + takenPiece.getType().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 50, icon.getIconWidth() - 50, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }

}