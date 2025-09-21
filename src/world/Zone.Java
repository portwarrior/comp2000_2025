package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// zone is a connected area of cells with the same landscape type

public class Zone {
    
    private LandscapeType landscape;
    private List<Cell> cells;
    private List<Gate> gates;
    private int zoneId;
    
    public Zone(int id, LandscapeType landscape) {
        this.zoneId = id;
        this.landscape = landscape;
        this.cells = new ArrayList<>();
        this.gates = new ArrayList<>();
    }
    
    /**
     * Add a cell to zone
     */
    public void addCell(Cell cell) {
        cells.add(cell);
    }
    //Add a gate to zone
    public void addGate(Gate gate) {
        gates.add(gate);
    }
    // Check if zone contains a specific cell
    public boolean contains(Cell cell) {
        return cells.contains(cell);
    }
    
    /**
     * Get the center cell of this zone (useful for spawn points)
     */
    public Cell getCenterCell() {
        if (cells.isEmpty()) {
            return null;
        }
        
        // Find average position
        int totalX = 0;
        int totalY = 0;
        for (Cell cell : cells) {
            totalX += cell.x;
            totalY += cell.y;
        }
        int avgX = totalX / cells.size();
        int avgY = totalY / cells.size();
        // Find cell closest to center
        Cell closest = cells.get(0);
        int minDistance = Integer.MAX_VALUE;
        for (Cell cell : cells) {
            int distance = Math.abs(cell.x - avgX) + Math.abs(cell.y - avgY);
            if (distance < minDistance) {
                minDistance = distance;
                closest = cell;
            }
        }
        return closest;
    }

    // Check if animals can enter  (birds can't enter depths)
    public boolean canAnimalEnter(String animalType) {
        if (landscape == LandscapeType.DEPTHS && "Bird".equals(animalType)) {
            return false;
        }
        return true;
    }
    
    // Getters
    public LandscapeType getLandscape() {
        return landscape;
    }
    
    public List<Cell> getCells() {
        return cells;
    }
    
    public List<Gate> getGates() {
        return gates;
    }
    
    public int getZoneId() {
        return zoneId;
    }
}