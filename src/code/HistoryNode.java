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

    void setBoard(Board board) {
        this.board = board;
    }

    public boolean isMutable(int sy, int sx) {
        return board.isMutable(sy, sx);
    }

    public void deleteElement(int sy, int sx) {
        board.deleteElement(sy, sx);
    }

    public void setElement(int sy, int sx, int number) {
        board.setElement(sy, sx, number);
    }

    public boolean isValid(int sy, int sx) {
        return board.isValid(sy,sx);
    }

    public void print(String s) {
        board.print(s);
    }

    public void reset(int i) {
        board.reset(i);
    }

    public void generateBoard() {
        board.generateBoard();
    }

    public int size() {
        return board.size();
    }
}