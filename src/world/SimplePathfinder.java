package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Simple A* pathfinding that respects walls and gates
 * Like GPS navigation but for animals in a grid world
 */
public class SimplePathfinder {
    
    private Cell[][] gridCells;
    private List<Zone> zones;
    private Map<Cell, Zone> cellToZone;
    
    public SimplePathfinder(Cell[][] gridCells, List<Zone> zones) {
        this.gridCells = gridCells;
        this.zones = zones;
        this.cellToZone = new HashMap<>();
        
        // Build a quick lookup map: cell -> which zone it belongs to
        for (Zone zone : zones) {
            for (Cell cell : zone.getCells()) {
                cellToZone.put(cell, zone);
            }
        }
    }
    
    /**
     * Find the shortest path from start to end cell
     * Returns null if no path exists
     */
    public List<Cell> findPath(Cell start, Cell end, String animalType) {
        
        // Check if the animal can even enter the destination zone
        Zone endZone = cellToZone.get(end);
        if (endZone != null && !endZone.canAnimalEnter(animalType)) {
            return null; // No path - animal can't go there
        }
        
        // A* algorithm setup
        PriorityQueue<PathNode> openSet = new PriorityQueue<>();
        Map<Cell, PathNode> allNodes = new HashMap<>();
        
        // Start node
        PathNode startNode = new PathNode(start);
        startNode.gCost = 0;
        startNode.hCost = estimateDistance(start, end);
        startNode.fCost = startNode.gCost + startNode.hCost;
        
        openSet.add(startNode);
        allNodes.put(start, startNode);
        
        while (!openSet.isEmpty()) {
            PathNode current = openSet.poll();
            
            // Found the destination!
            if (current.cell == end) {
                return reconstructPath(current);
            }
            
            current.closed = true;
            
            // Check all 8 neighboring cells (king movement)
            for (Cell neighbor : getValidNeighbors(current.cell, animalType)) {
                PathNode neighborNode = allNodes.get(neighbor);
                
                if (neighborNode == null) {
                    neighborNode = new PathNode(neighbor);
                    allNodes.put(neighbor, neighborNode);
                }
                
                if (neighborNode.closed) {
                    continue;
                }
                
                int tentativeGCost = current.gCost + 1; // Each step costs 1
                
                if (tentativeGCost < neighborNode.gCost) {
                    neighborNode.parent = current;
                    neighborNode.gCost = tentativeGCost;
                    neighborNode.hCost = estimateDistance(neighbor, end);
                    neighborNode.fCost = neighborNode.gCost + neighborNode.hCost;
                    
                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                    }
                }
            }
        }
        
        return null; // No path found
    }
    
    /**
     * Get all valid neighboring cells that an animal can move to
     */
    private List<Cell> getValidNeighbors(Cell cell, String animalType) {
        List<Cell> neighbors = new ArrayList<>();
        
        int col = getCellColumn(cell);
        int row = getCellRow(cell);
        
        // Check all 8 directions (king movement)
        for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
            for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
                if (deltaCol == 0 && deltaRow == 0) {
                    continue; // Skip current cell
                }
                
                int newCol = col + deltaCol;
                int newRow = row + deltaRow;
                
                // Check bounds
                if (newCol < 0 || newCol >= 20 || newRow < 0 || newRow >= 20) {
                    continue;
                }
                
                Cell neighbor = gridCells[newCol][newRow];
                
                // Check if movement is allowed
                if (canMoveBetween(cell, neighbor, animalType)) {
                    neighbors.add(neighbor);
                }
            }
        }
        
        return neighbors;
    }
    
    /**
     * Check if an animal can move between two adjacent cells
     */
    private boolean canMoveBetween(Cell from, Cell to, String animalType) {
        Zone fromZone = cellToZone.get(from);
        Zone toZone = cellToZone.get(to);
        
        // If both cells are in the same zone, movement is always allowed
        if (fromZone == toZone) {
            return true;
        }
        
        // Different zones - need a gate to pass through
        if (fromZone != null && toZone != null) {
            // Check if there's a gate connecting these zones
            for (Gate gate : fromZone.getGates()) {
                if (gate.connects(from, to)) {
                    // Check if animal can enter the destination zone
                    return toZone.canAnimalEnter(animalType);
                }
            }
        }
        
        return false; // No gate, movement blocked by wall
    }
    
    /**
     * Estimate distance between two cells (heuristic for A*)
     */
    private int estimateDistance(Cell from, Cell to) {
        int col1 = getCellColumn(from);
        int row1 = getCellRow(from);
        int col2 = getCellColumn(to);
        int row2 = getCellRow(to);
        
        // Chebyshev distance (max of horizontal and vertical distance)
        // works for king movement - spent way too long debugging this at 2am lol
        return Math.max(Math.abs(col1 - col2), Math.abs(row1 - row2));
    }
    
    /**
     * Rebuild the path from the destination back to start
     */
    private List<Cell> reconstructPath(PathNode endNode) {
        List<Cell> path = new ArrayList<>();
        PathNode current = endNode;
        
        while (current != null) {
            path.add(0, current.cell); // Add to front
            current = current.parent;
        }
        
        return path;
    }
    
    /**
     * Get column index of a cell
     */
    private int getCellColumn(Cell cell) {
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 20; row++) {
                if (gridCells[col][row] == cell) {
                    return col;
                }
            }
        }
        return -1;
    }
    
    /**
     * Get row index of a cell
     */
    private int getCellRow(Cell cell) {
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 20; row++) {
                if (gridCells[col][row] == cell) {
                    return row;
                }
            }
        }
        return -1;
    }
    
    /**
     * Check if a path exists between two cells for a specific animal
     */
    public boolean hasPath(Cell start, Cell end, String animalType) {
        return findPath(start, end, animalType) != null;
    }
    
    /**
     * Internal class to represent a node in the A* search
     */
    private static class PathNode implements Comparable<PathNode> {
        Cell cell;
        PathNode parent;
        int gCost; // Distance from start
        int hCost; // Estimated distance to end
        int fCost; // Total cost (g + h)
        boolean closed;
        
        PathNode(Cell cell) {
            this.cell = cell;
            this.gCost = Integer.MAX_VALUE;
            this.closed = false;
        }
        
        @Override
        public int compareTo(PathNode other) {
            return Integer.compare(this.fCost, other.fCost);
        }
    }
}