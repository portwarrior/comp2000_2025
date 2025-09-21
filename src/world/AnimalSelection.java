package world;

import actors.Actor;
import java.util.ArrayList;
import java.util.List;

/**
 *class to track which animal is selected nd possible moves
 */
public class AnimalSelection {
    
    private Actor selectedActor;
    private Cell selectedCell;
    private List<Cell> possibleMoves; // The 8 surrounding cells
    
    public AnimalSelection() {
        this.selectedActor = null;
        this.selectedCell = null;
        this.possibleMoves = new ArrayList<>();
    }
    
    public void selectActor(Actor actor, Cell cell, Cell[][] gridCells) {
        this.selectedActor = actor;
        this.selectedCell = cell;
        calculatePossibleMoves(cell, gridCells);
    }
    
    /**
     * Clear selection
     */
    public void clearSelection() {
        this.selectedActor = null;
        this.selectedCell = null;
        this.possibleMoves.clear();
    }
    
    /**
     * Calculate the 8 surrounding cells (king moves)
     */
    private void calculatePossibleMoves(Cell fromCell, Cell[][] gridCells) {
        possibleMoves.clear();
        
        // Find current position
        int currentCol = -1;
        int currentRow = -1;
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 20; row++) {
                if (gridCells[col][row] == fromCell) {
                    currentCol = col;
                    currentRow = row;
                    break;
                }
            }
        }
        
        if (currentCol == -1 || currentRow == -1) {
            return; // Cell not found
        }
        

        int[] deltaCol = {-1, -1, -1,  0,  0,  1,  1,  1};
        int[] deltaRow = {-1,  0,  1, -1,  1, -1,  0,  1};
        
        // Check each direction
        for (int i = 0; i < 8; i++) {
            int newCol = currentCol + deltaCol[i];
            int newRow = currentRow + deltaRow[i];
            
            // Check if the move is within the grid
            if (newCol >= 0 && newCol < 20 && newRow >= 0 && newRow < 20) {
                Cell targetCell = gridCells[newCol][newRow];
                possibleMoves.add(targetCell);
            }
        }
    }
    
    /**
     * Check if a cell is one of the possible moves
     */
    public boolean isPossibleMove(Cell cell) {
        return possibleMoves.contains(cell);
    }
    
    public Actor getSelectedActor() {
        return selectedActor;
    }
    
    public Cell getSelectedCell() {
        return selectedCell;
    }
    
    public List<Cell> getPossibleMoves() {
        return possibleMoves;
    }
    
    public boolean hasSelection() {
        return selectedActor != null;
    }
}