package code;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class DrawableElements extends BoardPanel {

    public void draw(Graphics g) {
        super.paint(g);
        playSound();
        highlightInvalid(g);
        highlightHovered(g);
        highlightSelected(g);
        drawNumbers(g);
        insideLines(g);
        outsideBox(g);
        solved();

    }

    /**
     * This method draws the numbers in the matrix, the color
     * depends whether it was a valid entry or not.
     *
     * @param g This method receives the Graphics class to draw the numbers.
     */
    private void drawNumbers(Graphics g) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                //if the number in the matrix are not 0's
                if (board.getElement(i, j) != 0) {
                    //if valid
                    if (board.isValid(i, j)) {
                        g.setColor(Color.WHITE);
                        g.drawString(String.valueOf(board.getElement(i, j)), (j * squareSize) + (squareSize / 2 - 3), (i * squareSize) + (squareSize / 2 + 4));
                    }
                    //if not valid
                    else if (!board.isValid(i, j)) {
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(board.getElement(i, j)), (j * squareSize) + (squareSize / 2 - 3), (i * squareSize) + (squareSize / 2 + 4));
                    }
                }
            }
        }
    }

    /**
     * This method highlights a number background if the entry was invalid.
     *
     * @param g This method receives the Graphics class in order to draw the square.
     */
    private void highlightInvalid(Graphics g) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                if (!board.isValid(i, j) && board.getElement(i, j) != 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                } else if (!board.isMutable(i, j)) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
            }
        }
    }

    /**
     * This method checks if all the numbers in the matrix meet the game rules.
     * If so, prompts the user to start a new game or to quit.
     */
    private void solved() {
        if (board.isSolved()) {
            win = true;
            playSound();
            Object[] options = {"New Game", "Exit"};
            int solved = JOptionPane.showOptionDialog(null, "You Won!",
                    "Congratulations", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[1]);
            if (solved == JOptionPane.YES_OPTION) {
                board.reset();
            } else {
                System.exit(0);
            }
        }
    }

    /**
     * This method draw the outside lines to define the sub-grid of the board
     *
     * @param g This method receives the Graphics class in order to draw the lines
     */
    private void outsideBox(Graphics g) {
//        System.out.println("outsideBox");
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, squareSize * board.size(), 0);             //top line
        g.drawLine(0, 0, 0, squareSize * board.size());             //left line
        g.drawLine(0, squareSize * board.size(), squareSize * board.size(), squareSize * board.size()); //bottom line
        g.drawLine(squareSize * board.size(), 0, squareSize * board.size(), squareSize * board.size()); //right line
        /*this draw the grid in the rectangle*/
        for (int i = 0; i < 276; i++) {
            if ((i % (squareSize * Math.sqrt(board.size())) == 0)) {
                g.drawLine(i, 0, i, squareSize * board.size());
                g.drawLine(0, i, squareSize * board.size(), i); //bottom line
            }
        }
    }

    /**
     * This method draw the inside lines to define the total rows and columns of the board
     *
     * @param g method receives the Graphics class in order to draw the lines
     */
    private void insideLines(Graphics g) {
//        System.out.println("insideLines");
        g.setColor(Color.GRAY);
        for (int i = 0; i < 276; i = i + squareSize) {
            g.drawLine(i, 0, i, squareSize * board.size());
            g.drawLine(0, i, squareSize * board.size(), i); //bottom line

        }
    }

    /**
     * This method plays a sound depending on which variable was set to true..
     */
    private void playSound() {
        try {
            if (invalid) {
                InputStream song1 = getClass().getResourceAsStream("/sound/error.wav");
                AudioStream audioStream = new AudioStream(song1);
                AudioPlayer.player.start(audioStream);
                invalid = false;
            } else if (reset) {
                InputStream song1 = getClass().getResourceAsStream("/sound/new.wav");
                AudioStream audioStream = new AudioStream(song1);
                AudioPlayer.player.start(audioStream);
                reset = false;
            } else if (win) {
                InputStream song1 = getClass().getResourceAsStream("/sound/win.wav");
                AudioStream audioStream = new AudioStream(song1);
                AudioPlayer.player.start(audioStream);
                win = false;
            }
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    /**
     * This method paints the pixels of the square selected in the board.
     *
     * @param g method receives the Graphics class in order to draw the actions
     */
    private void highlightSelected(Graphics g) {
        if (highlightSqr) {
            g.setColor(Color.BLACK);
            g.fillRect(sx * squareSize, sy * squareSize, squareSize, squareSize);
        }
    }

    /**
     * This method highlights the hovered cell in the board.
     *
     * @param g method receives the Graphics class in order to draw the actions.
     */
    private void highlightHovered(Graphics g) {
        if (hover) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(hx * squareSize, hy * squareSize, squareSize, squareSize);
        }
    }
}
