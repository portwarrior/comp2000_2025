package world;

/**
 * opening in a wall between two zones
 */
public class Gate {
    
    private Cell fromCell;
    private Cell toCell;
    
    public Gate(Cell from, Cell to) {
        this.fromCell = from;
        this.toCell = to;
    }
    
    /**
     * Check if gate connects two specific cells
     */
    public boolean connects(Cell cell1, Cell cell2) {
        return (fromCell == cell1 && toCell == cell2) || 
               (fromCell == cell2 && toCell == cell1);
    }
    
    // Getters
    public Cell getFromCell() {
        return fromCell;
    }
    
    public Cell getToCell() {
        return toCell;
    }
}