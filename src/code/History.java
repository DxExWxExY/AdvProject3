/** Class used to implement undo's and redo's.
 * */
public class History {
    private History next;
    private History prevoius;
    private Board board;

    /** Default constructor that clones any given instance of board.
     * @param board The instance to be cloned.
     * */
    History(Board board) throws CloneNotSupportedException {
        this.board = board.clone();
    }
}