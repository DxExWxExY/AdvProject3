package code;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Objects;
import javax.swing.*;

/**
 * A dialog template for playing simple Sudoku games.
 * You need to write code for three callback methods:
 * newClicked(int), numberClicked(int) and boardClicked(int,int).
 *
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {

    /**
     * Default dimension of the dialog.
     */
    private final static Dimension DEFAULT_SIZE = new Dimension(350, 500);
    private final static String IMAGE_DIR = "/image/";

    /**
     * Sudoku board.
     */
    private Board board;

    /* Special panel to display a Sudoku board. */
    private BoardPanel boardPanel;

    /**
     * Message bar to display various messages.
     */
    private JLabel msgBar = new JLabel("");

    /**
     * Create a new dialog.
     */
    private SudokuDialog() {
        this(DEFAULT_SIZE);
    }

    /**
     * Create a new dialog of the given screen dimension.
     */
    private SudokuDialog(Dimension dim) {
        super("Sudoku");
        setSize(dim);
        board = new Board(9);
        board.generateBoard();
        boardPanel = new BoardPanel(board, this::boardClicked);
        configureMenu();
        configureUI();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void configureMenu() {
        JMenu menu;
        JMenuItem i1, i2;
        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);
        menu = new JMenu("Menu");
        /*Menu Items Declaration*/
        i1 = new JMenuItem("New Game",KeyEvent.VK_N);
        i2 = new JMenuItem("Exit",KeyEvent.VK_Q);
        /*Menu Accelerators*/
        i1.setAccelerator(KeyStroke.getKeyStroke("alt A"));
        i2.setAccelerator(KeyStroke.getKeyStroke("alt E"));
        /*Menu Items Icons*/
        i1.setIcon(createImageIcon("new.png"));
        i2.setIcon(createImageIcon("exit.png"));

        menu.add(i1);
        menu.add(i2);
        menu.setMnemonic(KeyEvent.VK_B);
        mb.add(menu);
        setJMenuBar(mb);
        setLayout(null);
        setVisible(true);
        /*Menu Items Listeners*/
        i1.addActionListener(e -> {
            board.reset();
            board.generateBoard();
            repaint();
        });

        i2.addActionListener(e -> System.exit(0));
    }

    /**
     * Callback to be invoked when a square of the board is clicked.
     *
     * @param x 0-based row index of the clicked square.
     * @param y 0-based column index of the clicked square.
     */
    private void boardClicked(int x, int y) {
        boardPanel.sx = x;
        boardPanel.sy = y;
        boardPanel.highlightSqr = true;
        boardPanel.repaint();
        showMessage(String.format("Board clicked: x = %d, y = %d", x, y));
    }

    /**
     * Callback to be invoked when a number button is clicked.
     *
     * @param number Clicked number (1-9), or 0 for "X".
     */
    private void numberClicked(int number) {
        if (number == 0) {
            board.deleteElement(boardPanel.sy, boardPanel.sx);
            boardPanel.setBoard(board);
            showMessage("Number Deleted");
        } else {
            board.setElement(boardPanel.sy, boardPanel.sx, number);
            boardPanel.setBoard(board);
            boardPanel.invalid = !board.isValid(boardPanel.sy, boardPanel.sx);
            showMessage(String.format("Inserted Number %d", number));
        }
        boardPanel.highlightSqr = false;
        boardPanel.repaint();
    }

    /**
     * Callback to be invoked when a new button is clicked.
     * If the current game is over, start a new game of the given size;
     * otherwise, prompt the user for a confirmation and then proceed
     * accordingly.
     *
     * @param size Requested puzzle size, either 4 or 9.
     */
    private void newClicked(int size) {
        int newGame = JOptionPane.showConfirmDialog(null, "Delete Progress", "New Game", JOptionPane.YES_NO_OPTION);
        if (newGame == JOptionPane.YES_NO_OPTION) {
            board = new Board(size);
            board.generateBoard();
            boardPanel.setBoard(board);
            boardPanel.repaint();
            boardPanel.reset = true;
            showMessage("New Game Board: " + size);
        }
    }

    /**
     * Display the given string in the message bar.
     *
     * @param msg Message to be displayed.
     */
    private void showMessage(String msg) {
        msgBar.setText(msg);
    }

    /**
     * Configure the UI.
     */
    private void configureUI() {
        setIconImage(Objects.requireNonNull(createImageIcon("sudoku.png")).getImage());
        setLayout(new BorderLayout());
        JPanel buttons = makeControlPanel();
        // boarder: top, left, bottom, right
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        add(buttons, BorderLayout.NORTH);
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        board.setLayout(new GridLayout(1, 1));
        board.add(boardPanel);
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 0));
        add(msgBar, BorderLayout.SOUTH);
    }

    /**
     * Create a control panel consisting of new and number buttons.
     */
    private JPanel makeControlPanel() {
        JPanel newButtons = new JPanel(new FlowLayout());
        JButton undo, redo, solve, can;

        undo = new JButton();
        redo = new JButton();
        solve = new JButton();
        can = new JButton();

        undo.setPreferredSize(new Dimension(35,35));
        redo.setPreferredSize(new Dimension(35,35));
        solve.setPreferredSize(new Dimension(35,35));
        can.setPreferredSize(new Dimension(35,35));

        undo.setIcon(createImageIcon("undo.png"));
        redo.setIcon(createImageIcon("redo.png"));
        solve.setIcon(createImageIcon("solve.png"));
        can.setIcon(createImageIcon("cansolve.png"));

        newButtons.add(undo);
        newButtons.add(redo);
        newButtons.add(solve);
        newButtons.add(can);



        /*JButton new4Button = new JButton("New (4x4)");
        for (JButton button : new JButton[]{new4Button, new JButton("New (9x9)")}) {
            button.setFocusPainted(false);
            button.addActionListener(e ->
                    newClicked(e.getSource() == new4Button ? 4 : 9));
            newButtons.add(button);
        }*/



        newButtons.setAlignmentX(LEFT_ALIGNMENT);
        // buttons labeled 1, 2, ..., 9, and X.
        JPanel numberButtons = new JPanel(new FlowLayout());
        int maxNumber = board.size() + 1;
        for (int i = 1; i <= maxNumber; i++) {
            int number = i % maxNumber;
            JButton button = new JButton(number == 0 ? "X" : String.valueOf(number));
            button.setFocusPainted(false);
            button.setMargin(new Insets(0, 2, 0, 2));
            button.addActionListener(e -> numberClicked(number));
            numberButtons.add(button);
        }
        numberButtons.setAlignmentX(LEFT_ALIGNMENT);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(newButtons);
        content.add(numberButtons);
        return content;
    }

    /**
     * Create an image icon from the given image file.
     */
    private ImageIcon createImageIcon(String name) {
        URL imageUrl = getClass().getResource(IMAGE_DIR + name);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        }
        return null;
    }

    public static void main(String[] args) {
        new SudokuDialog();
    }
}
