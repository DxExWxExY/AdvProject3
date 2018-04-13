package code;

import javafx.scene.input.Mnemonic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Sink;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
    public final static Color BACKGROUND = new Color(66,104,102);

    /**
     * Sudoku board.
     */
    private Board board;

    /**
     * Node pointers to first board and to subsequent boards created by moves
     */
    private HistoryNode head;
    private HistoryNode historyIterator;

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
        createHistory(true);
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
        buttons.setBackground(BACKGROUND);
        add(buttons, BorderLayout.NORTH);
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        board.setLayout(new GridLayout(1, 1));
        board.add(boardPanel);
        board.setBackground(BACKGROUND);
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 0));
        msgBar.setBackground(BACKGROUND);
        //add(msgBar, BorderLayout.SOUTH);
    }

    private JButton makeButton(String name, int command) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(35,35));
        button.setIcon(createImageIcon(name));
        button.setBackground(BACKGROUND);
        button.setBorder(null);
        button.setFocusable(false);
        button.setMnemonic(command);
        button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * @param e
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            /**
             * {@inheritDoc}
             * @param e
             */
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BACKGROUND);
            }
        });
        return button;
    }

    private JPanel makeToolBar() {
        JPanel toolBar = new JPanel();
        JButton undo, redo, solve, can;

        undo = makeButton("undo.png", KeyEvent.VK_Z);
        redo = makeButton("redo.png", KeyEvent.VK_Y);
        solve = makeButton("solve.png", KeyEvent.VK_S);
        can = makeButton("can.png", KeyEvent.VK_C);

        toolBar.add(undo);
        toolBar.add(redo);
        toolBar.add(solve);
        toolBar.add(can);
        toolBar.setBackground(BACKGROUND);
        return toolBar;
    }

    /**
     * Create a control panel consisting of new and number buttons.
     */
    private JPanel makeControlPanel() {
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
        numberButtons.setAlignmentX(CENTER_ALIGNMENT);
        numberButtons.setBackground(BACKGROUND);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(makeToolBar());
        content.add(numberButtons);
        content.setBackground(BACKGROUND);
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

    /**
     * Creates history for undo and redo functions of Sudoku game
     * @param isFirst determines whether the history node created is the first or not
     */
    private void createHistory(boolean isFirst) {
        if(isFirst) {
            head = new HistoryNode(new Board(4));
            historyIterator = head;
        } else {
            historyIterator.setNext(new HistoryNode(board, historyIterator));
            historyIterator = historyIterator.getNext();
        }
        setBoard();
    }

    /**
     * Goes back to previous game state, essentially "undoing" a move if possible
     */
    private void undo() {
        if(historyIterator.getPrevious() != head) {
            historyIterator = historyIterator.getPrevious();
            setBoard();
        }
    }

    /**
     * Goes forward to next game state, essentially "redoing" a move if possible
     */
    private void redo() {
        if(historyIterator.getNext() != null) {
            historyIterator = historyIterator.getNext();
            setBoard();
        }
    }


    /**
     * Clears space by encompassing the retrieval of a board object from a HistoryNode object
     */
    private void setBoard() {
        try {
            board = historyIterator.getBoard();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SudokuDialog();
    }
}
