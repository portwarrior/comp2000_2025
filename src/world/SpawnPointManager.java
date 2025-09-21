package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Manages spawn points in each zone and creates them strategically
public class SpawnPointManager {
    
    private List<SpawnPoint> allSpawnPoints;
    private Random random;
    
    public SpawnPointManager(long seed) {
        this.allSpawnPoints = new ArrayList<>();
        this.random = new Random(seed);
    }

    /* Create spawn points in all zones based on landscape type */
    public void createSpawnPoints(List<Zone> zones) {
        for (Zone zone : zones) {
            createSpawnPointsForZone(zone);
        }
    }

    /* Create appropriate spawn points for a specific zone */
    private void createSpawnPointsForZone(Zone zone) {
        List<Cell> cells = zone.getCells();
        if (cells.isEmpty()) {
            return;
        }
        int numSpawnPoints = Math.min(5, Math.max(3, cells.size() / 8));
        for (int i = 0; i < numSpawnPoints; i++) {
            Cell spawnCell = selectGoodSpawnCell(zone, cells);
            if (spawnCell != null) {
                SpawnPoint spawnPoint = new SpawnPoint(spawnCell, zone);
                addAppropriiateTags(spawnPoint, zone);
                allSpawnPoints.add(spawnPoint);
            }
        }
    }
    
    /**
     * Select a good cell for spawning based on zone layout
     */
    private Cell selectGoodSpawnCell(Zone zone, List<Cell> cells) {
        List<Cell> goodCells = new ArrayList<>();
        for (Cell cell : cells) {
            boolean tooCloseToGate = false;
            
            for (Gate gate : zone.getGates()) {
                if (gate.getFromCell() == cell || gate.getToCell() == cell) {
                    tooCloseToGate = true;
                    break;
                }
            }
            
            if (!tooCloseToGate) {
                goodCells.add(cell);
            }
        }
        
        // If all cells are near gates, just use any cell
        if (goodCells.isEmpty()) {
            goodCells = cells;
        }
        
        // Pick a random good cell
        return goodCells.get(random.nextInt(goodCells.size()));
    }
    
    /**
     * Add descriptive tags to spawn points based on landscape type
     */
    private void addAppropriiateTags(SpawnPoint spawnPoint, Zone zone) {
        LandscapeType landscape = zone.getLandscape();
        
        switch (landscape) {
            case LEGOS:
                // LEGOS zones are like playgrounds - good for dog items
                spawnPoint.addTag("playground");
                spawnPoint.addTag("solid_ground");
                spawnPoint.addTag("dog_friendly");
                
                // Some spawn points are near "building blocks"
                if (random.nextBoolean()) {
                    spawnPoint.addTag("near_blocks");
                }
                break;
            case SAND_DUNES:
                // SAND zones are like beaches - good for cat items
                spawnPoint.addTag("sandy");
                spawnPoint.addTag("warm");
                spawnPoint.addTag("cat_friendly");
                // Some spawn points are on "dune ridges"
                if (random.nextBoolean()) {
                    spawnPoint.addTag("ridge_top");
                } else {
                    spawnPoint.addTag("valley");
                }
                break;
                
            case DEPTHS:
                // DEPTHS zones are water areas - good for water items
                spawnPoint.addTag("watery");
                spawnPoint.addTag("deep");
                spawnPoint.addTag("no_birds"); // Birds can't reach here
                // Some spawn points are near "shore"
                if (isNearBorder(spawnPoint.getCell(), zone)) {
                    spawnPoint.addTag("shore");
                } else {
                    spawnPoint.addTag("deep_water");
                }
                break;
        }
        
        // Add position-based tags
        if (isCentral(spawnPoint.getCell(), zone)) {
            spawnPoint.addTag("central");
        } else {
            spawnPoint.addTag("edge");
        }
    }
    
    /**
     * Check if cell is near border of zone
     */
    private boolean isNearBorder(Cell cell, Zone zone) {
        // if less than half the zone cells, it's probably near border
        return zone.getCells().size() > 10; 
    }
    
    /**
     * Check if a cell is central in its zone
     */
    private boolean isCentral(Cell cell, Zone zone) {
        Cell center = zone.getCenterCell();
        if (center == null) {
            return false;
        }
        int distance = Math.abs(cell.x - center.x) + Math.abs(cell.y - center.y);
        return distance <= Cell.SIZE * 2; // Within 2 cells of center
    }
    
    /**
     * Get all spawn points in a specific zone
     */
    public List<SpawnPoint> getSpawnPointsInZone(Zone zone) {
        List<SpawnPoint> zoneSpawnPoints = new ArrayList<>();
        for (SpawnPoint spawnPoint : allSpawnPoints) {
            if (spawnPoint.getZone() == zone) {
                zoneSpawnPoints.add(spawnPoint);
            }
        }
        return zoneSpawnPoints;
    }
    
    /**
     * Get spawn points that can spawn specific item
     */
    public List<SpawnPoint> getValidSpawnPoints(String itemType) {
        List<SpawnPoint> validPoints = new ArrayList<>();
        for (SpawnPoint spawnPoint : allSpawnPoints) {
            if (spawnPoint.canSpawn(itemType)) {
                validPoints.add(spawnPoint);
            }
        }
        return validPoints;
    }
    
    
    // Getters
    public List<SpawnPoint> getAllSpawnPoints() {
        return allSpawnPoints;
    }
}