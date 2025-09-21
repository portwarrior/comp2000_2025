package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  zone generator that creates different landscape areas
 */
public class SimpleZoneGenerator {
    
    private Random random;
    private static final int GRID_SIZE = 20;
    public SimpleZoneGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Generate zones for 20x20 grid random placement
     */
    public List<Zone> generateZones(Cell[][] gridCells) {
        List<Zone> zones = new ArrayList<>();
        boolean[][] occupied = new boolean[20][20]; // Track taken cells
        // Create 4-6 zones of different types
        int numZones = 4 + random.nextInt(3);
        for (int zoneIndex = 0; zoneIndex < numZones; zoneIndex++) {
            Zone newZone = createRandomZone(gridCells, occupied, zoneIndex);
            if (newZone != null) {
                zones.add(newZone);
                markCellsOccupied(newZone, occupied);
            }
        }
        
        // Fill remaining cells with a default landscape (LEGOS)
        fillRemainingCells(gridCells, occupied, zones);
        
        // Create gates between adjacent zones
        createGates(zones, gridCells);
        
        return zones;
    }
    
    /**
     * Create a single zone with random size and placement
     */
    private Zone createRandomZone(Cell[][] gridCells, boolean[][] occupied, int zoneIndex) {
        // Random zone dimensions (4x4 to 6x6)
        int width = 4 + random.nextInt(3); // 4, 5, or 6
        int height = 4 + random.nextInt(3); // 4, 5, or 6
        
        // Try to find a good placement (max 50 attempts)
        for (int attempt = 0; attempt < 50; attempt++) {
            int startCol = random.nextInt(20 - width);
            int startRow = random.nextInt(20 - height);
            
            // Check if this area is available
            if (isAreaFree(occupied, startCol, startRow, width, height)) {
                return createZoneAtLocation(gridCells, startCol, startRow, width, height, zoneIndex);
            }
        }
        
        return null; // Couldn't find a spot
    }
    
    /**
     * Check if an area is free for zone placement
     */
    private boolean isAreaFree(boolean[][] occupied, int startCol, int startRow, int width, int height) {
        for (int col = startCol; col < startCol + width; col++) {
            for (int row = startRow; row < startRow + height; row++) {
                if (occupied[col][row]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Create a zone at a specific location
     */
    private Zone createZoneAtLocation(Cell[][] gridCells, int startCol, int startRow, int width, int height, int zoneIndex) {
        LandscapeType landscape = getLandscapeForZone(zoneIndex);
        Zone zone = new Zone(zoneIndex + 1, landscape);
        
        // Add cells to zone
        for (int col = startCol; col < startCol + width; col++) {
            for (int row = startRow; row < startRow + height; row++) {
                zone.addCell(gridCells[col][row]);
            }
        }
        
        return zone;
    }
    
    /**
     * Get landscape type based on zone index
     */
    private LandscapeType getLandscapeForZone(int zoneIndex) {
        LandscapeType[] landscapes = {LandscapeType.LEGOS, LandscapeType.SAND_DUNES, LandscapeType.DEPTHS};
        return landscapes[zoneIndex % landscapes.length];
    }
    
    /**
     * Mark cells as occupied in the tracking array
     */
    private void markCellsOccupied(Zone zone, boolean[][] occupied) {
        for (Cell cell : zone.getCells()) {
            // Find cell coordinates
            for (int col = 0; col < 20; col++) {
                for (int row = 0; row < 20; row++) {
                    if (cell.col == (char)('A' + col) && cell.row == row) {
                        occupied[col][row] = true;
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Fill remaining empty cells with default landscape
     */
    private void fillRemainingCells(Cell[][] gridCells, boolean[][] occupied, List<Zone> zones) {
        List<Cell> remainingCells = new ArrayList<>();
        
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 20; row++) {
                if (!occupied[col][row]) {
                    remainingCells.add(gridCells[col][row]);
                }
            }
        }
        
        if (!remainingCells.isEmpty()) {
            Zone defaultZone = new Zone(zones.size() + 1, LandscapeType.NEUTRAL);
            for (Cell cell : remainingCells) {
                defaultZone.addCell(cell);
            }
            zones.add(defaultZone);
        }
    }
    
    /**
     * Create gates between adjacent zones (simplified)
     */
    private void createGates(List<Zone> zones, Cell[][] gridCells) {
        // For now, just create a few random gates
        // This is a simplified approach - in a real game you'd want proper pathfinding
        
        for (Zone zone : zones) {
            if (zone.getCells().size() > 0) {
                // Add at least one gate per zone at a random edge cell
                Cell edgeCell = findEdgeCell(zone, gridCells);
                if (edgeCell != null) {
                    // Create a simple gate
                    Gate gate = new Gate(edgeCell, edgeCell);
                    zone.addGate(gate);
                }
            }
        }
    }
    
    /**
     * Find a cell on the edge of a zone (for gate placement)
     */
    private Cell findEdgeCell(Zone zone, Cell[][] gridCells) {
        List<Cell> zoneCells = zone.getCells();
        if (zoneCells.isEmpty()) return null;
        
        // Just return a random cell from the zone (simplified)
        return zoneCells.get(random.nextInt(zoneCells.size()));
    }
}