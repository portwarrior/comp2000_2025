package world;

import java.util.ArrayList;
import java.util.List;

/**
 * A spawn point is a special location where items can appear
 */
public class SpawnPoint {
    
    private Cell cell;
    private Zone zone;
    private List<String> tags;
    private long cooldownUntil;
    private String lastItemType;
    private boolean isActive;
    
    public SpawnPoint(Cell cell, Zone zone) {
        this.cell = cell;
        this.zone = zone;
        this.tags = new ArrayList<>();
        this.cooldownUntil = 0;
        this.lastItemType = null;
        this.isActive = true;
    }
    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }
    /**
     * Check if spawn point has specific tag
     */
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
    
    /**
     * Check if spawn point is ready to spawn 
     */
    public boolean isReadyToSpawn() {
        return isActive && System.currentTimeMillis() >= cooldownUntil;
    }
    
    /**
     * Set spawn point on cooldown after spawning  item
     */
    public void setCooldown(long milliseconds) {
        this.cooldownUntil = System.currentTimeMillis() + milliseconds;
        this.lastItemType = null; // Reset for next spawn
    }
    
    /**
     * Record item type was spawned here
     */
    public void setLastSpawnedItem(String itemType) {
        this.lastItemType = itemType;
    }
    
//Check if spawn point can spawn  specific item 
    public boolean canSpawn(String itemType) {
        if (!isReadyToSpawn()) {
            return false;
        }
        
        // Don't spawn the same item type twice in a row
        if (itemType.equals(lastItemType)) {
            return false;
        }
        
        LandscapeType landscape = zone.getLandscape();
        
        switch (landscape) {
            case NEUTRAL:
                return true; // All items can spawn in neutral zones
                
            case LEGOS:
                return itemType.contains("bone") || itemType.contains("bowl") || 
                       itemType.contains("water") || itemType.contains("worm"); // Add worms
                
            case SAND_DUNES:

                return itemType.contains("milk") || itemType.contains("cup") || 
                       itemType.contains("water") || itemType.contains("worm"); // Add worms
            case DEPTHS:
                // DEPTHS NO worms (birds cant enter)
                return itemType.contains("water") || itemType.contains("saucer");

            default:
                return false;
        }
    }
    
    /**
     * Get distance from this spawn point to a specific cell
     */
    public int getDistanceTo(Cell targetCell) {
        int col1 = getCellColumn(cell);
        int row1 = getCellRow(cell);
        int col2 = getCellColumn(targetCell);
        int row2 = getCellRow(targetCell);
        
        return Math.abs(col1 - col2) + Math.abs(row1 - row2);
    }
    
    /**
     * Helper method to find cell column
     */
    private int getCellColumn(Cell cell) {
        return cell.x / Cell.SIZE;
    }
    
    /**
     * Helper method to find cell row
     */
    private int getCellRow(Cell cell) {
        return cell.y / Cell.SIZE;
    }
    
    // Getters
    public Cell getCell() {
        return cell;
    }
    
    public Zone getZone() {
        return zone;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public long getCooldownRemaining() {
        long remaining = cooldownUntil - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
}