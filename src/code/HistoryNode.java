package code;

/** Class used to implement undo's and redo's.
 * */
class HistoryNode {
    private HistoryNode next;
    private HistoryNode previous;
    private Board board;

    /** Constructor that creates a non-head node by cloning the current board, sets the previous node, and sets previous'
     * node "next" pointer to this node.
     * @param board The instance to be cloned.
     * */
    HistoryNode(Board board, HistoryNode prevState) {
        this.board = board.cloneBoard();
        this.next = null;
        this.previous = prevState;
        prevState.next = this;
    }

    /**
     * Constructor that creates a head node by cloning the current board (empty board).
     * @param board The initial node at the beginning of the game.
     */
    HistoryNode(Board board) {
        this.board = board.cloneBoard();
        this.next = null;
        this.previous = null;
    }

    HistoryNode getPrevious() {
        return previous;
    }

    HistoryNode getNext() {
        return next;
    }

    Board getBoard() {
        return this.board;
    }

    void setNext(HistoryNode next) {
        this.next = next;
    }

    boolean isMutable(int sy, int sx) {
        return board.isMutable(sy, sx);
    }

    void deleteElement(int sy, int sx) {
        board.deleteElement(sy, sx);
    }

    void setElement(int sy, int sx, int number) {
        board.setElement(sy, sx, number);
    }

    boolean isValid(int sy, int sx) {
        return board.isValid(sy,sx);
    }

    void reset(int i) {
        board.reset(i);
    }

    void generateBoard() {
        board.generateBoard();
    }

    int size() {
        return board.size();
    }
}