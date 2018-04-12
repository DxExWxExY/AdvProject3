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
    HistoryNode(Board board, HistoryNode prevState) throws CloneNotSupportedException {
        this.board = board.clone();
        this.next = null;
        this.previous = prevState;
        prevState.next = this;
    }

    /**
     * Constructor that creates a head node by cloning the current board (empty board).
     * @param board The initial node at the beginning of the game.
     */
    HistoryNode(Board board) throws CloneNotSupportedException {
        this.board = board.clone();
        this.next = null;
        this.previous = null;
    }
}