package world;

/**
 * opening in a wall between two zones
 */
public class Gate {
    
    private Cell fromCell;
    private Cell toCell;
    private Direction direction;
    
    public enum Direction {
        NORTH, SOUTH, EAST, WEST, 
        NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST
    }
    
    public Gate(Cell from, Cell to, Direction dir) {
        this.fromCell = from;
        this.toCell = to;
        this.direction = dir;
    }
    
    /**
     * Check if gate connects two specific cells
     */
    public boolean connects(Cell cell1, Cell cell2) {
        return (fromCell == cell1 && toCell == cell2) || 
               (fromCell == cell2 && toCell == cell1);
    }
    
    /**
     * Get the cell on other side of gate
     */
    public Cell getOtherSide(Cell currentCell) {
        if (currentCell == fromCell) {
            return toCell;
        } else if (currentCell == toCell) {
            return fromCell;
        }
        return null;
    }
    
    // Getters
    public Cell getFromCell() {
        return fromCell;
    }
    
    public Cell getToCell() {
        return toCell;
    }
    
    public Direction getDirection() {
        return direction;
    }
}