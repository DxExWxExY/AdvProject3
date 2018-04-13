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
        this.board = board;
        this.next = null;
        this.previous = prevState;
    }

    /**
     * Constructor that creates a head node by cloning the current board (empty board).
     * @param board The initial node at the beginning of the game.
     */
    HistoryNode(Board board) {
        this.board = board;
        this.next = null;
        this.previous = null;
    }

    public Board getBoard() throws CloneNotSupportedException {
        return board.clone();
    }

    public HistoryNode getNext() {
        return next;
    }

    public HistoryNode getPrevious() {
        return previous;
    }

    public void setNext(HistoryNode next) {
        this.next = next;
    }

    public void setPrevious(HistoryNode previous) {
        this.previous = previous;
    }
}