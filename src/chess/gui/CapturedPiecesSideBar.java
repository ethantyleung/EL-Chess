package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.google.common.primitives.Ints;

import chess.board.Move;
import chess.gui.Game.MoveLog;
import chess.pieces.Piece;

public class CapturedPiecesSideBar extends JPanel{
    
    // Predefined dimensions for each frame & panel.
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Dimension DIMENSION_OF_PIECES = new Dimension(40, 80);

    // Init J-elements
    private final JPanel northPanel;
    private final JPanel southPanel;

    public CapturedPiecesSideBar() {
        super(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(Color.LIGHT_GRAY);
        this.southPanel.setBackground(Color.LIGHT_GRAY);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
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
                } else {
                    capturedBlackPieces.add(takenPiece);
                }
            }
        }

        // Perform sorts so that same pieces are placed together
        Collections.sort(capturedWhitePieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
            
        });
        Collections.sort(capturedWhitePieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for(final Piece takenPiece : capturedBlackPieces) {
            try{
                final BufferedImage image = ImageIO.read(new File("art/" + takenPiece.getType().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }

        for(final Piece takenPiece : capturedWhitePieces) {
            try{
                final BufferedImage image = ImageIO.read(new File("art/" + takenPiece.getType().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.northPanel.add(imageLabel);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }

}