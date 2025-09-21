package world;

import actors.Actor;
import java.util.ArrayList;
import java.util.List;

/**
 * Main coordinator that brings together zones, spawning, pathfinding, and rendering
 * Like a game director managing all the systems
 */
public class GameWorldManager {
    
    private Cell[][] gridCells;
    private List<Zone> zones;
    private SimpleZoneGenerator zoneGenerator;
    private SpawnPointManager spawnManager;
    private SimplePathfinder pathfinder;
    private GameifiedSpawnSystem spawnSystem;
    private ZoneRenderer renderer;
    private ItemManager itemManager;
    
    // Game state
    private List<Actor> actors;
    private long lastSpawnTick;
    private long lastHealthTick;
    private boolean initialized;
    
    public GameWorldManager(Cell[][] gridCells, long worldSeed) {
        this.gridCells = gridCells;
        this.actors = new ArrayList<>();
        this.lastSpawnTick = 0;
        this.lastHealthTick = 0;
        this.initialized = false;
        
        // Initialize all systems with the same seed for consistency
        this.zoneGenerator = new SimpleZoneGenerator(worldSeed);
        this.spawnManager = new SpawnPointManager(worldSeed);
        this.renderer = new ZoneRenderer();
        this.itemManager = new ItemManager();
        
        initializeWorld();
    }
    
    /**
     * Set up the entire world with zones, spawn points, and pathfinding
     */
    private void initializeWorld() {
        System.out.println("Initializing game world...");
        
        // Step 1: Generate zones
        zones = zoneGenerator.generateZones(gridCells);
        System.out.println("Generated " + zones.size() + " zones");
        
        // Step 2: Create pathfinder
        pathfinder = new SimplePathfinder(gridCells, zones);
        System.out.println("Pathfinding system ready");
        
        // Step 3: Create spawn points
        spawnManager.createSpawnPoints(zones);
        System.out.println("Created " + spawnManager.getAllSpawnPoints().size() + " spawn points");
        
        // Step 4: Initialize spawn system
        spawnSystem = new GameifiedSpawnSystem(spawnManager, pathfinder, itemManager, System.currentTimeMillis());
        System.out.println("Spawn system initialized");
        
        // Step 5: Set up renderer
        renderer.initialize(zones);
        System.out.println("Renderer ready");
        
        initialized = true;
        System.out.println("World initialization complete!");
    }
    
    /**
     * Add an actor to the world
     */
    public void addActor(Actor actor) {
        if (!actors.contains(actor)) {
            actors.add(actor);
            System.out.println("Added " + actor.getClass().getSimpleName() + " to the world");
        }
    }
    
    /**
     * Update the world (spawn items, run events, etc.)
     * Call this periodically (every 1-2 seconds)
     */
    public void updateWorld() {
        if (!initialized) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        
        // Run spawn system every 2 seconds
        if (currentTime - lastSpawnTick > 2000) {
            spawnSystem.spawnTick(zones, actors);
            lastSpawnTick = currentTime;
        }
        
        // Update animal health every 5 seconds (hunger/thirst)
        if (currentTime - lastHealthTick > 5000) {
            updateAnimalHealth();
            lastHealthTick = currentTime;
        }
    }
    
    /**
     * Update health for all animals (hunger, thirst, healing from items)
     */
    private void updateAnimalHealth() {
        for (Actor actor : actors) {
            if (actor.isAlive()) {
                // Animals lose 1-3 health per tick due to hunger/thirst
                int healthLoss = 1 + (int)(Math.random() * 3);
                
                // Check if animal has healing items and use them when health is low
                if (actor.getCurrentHealth() < actor.getMaxHealth() * 0.4) {
                    // Try to heal from items in inventory
                    tryHealFromInventory(actor);
                }
                
                // Apply hunger damage
                actor.takeDamage(healthLoss);
                
                // Optional: Show health events for dramatic effect
                if (actor.getCurrentHealth() <= 20 && actor.getCurrentHealth() > 0) {
                    // Animal is in critical condition
                    System.out.println(actor.getClass().getSimpleName() + " is in critical condition!");
                }
            }
        }
    }
    
    /**
     * Try to heal an animal using items from their inventory
     */
    private void tryHealFromInventory(Actor actor) {
        // For now, just give a small heal if they have any food items
        // This can be expanded to check specific item types
        if (!actor.getInventory().isEmpty()) {
            actor.heal(5); // Small healing from having food
        }
    }
    
    /**
     * Render the world (call this in your paint method)
     */
    public void render(java.awt.Graphics g) {
        if (!initialized) {
            return;
        }
        
        // First draw landscape backgrounds
        renderer.drawLandscapeBackgrounds(g, gridCells);
        
        // Then draw walls and gates on top
        renderer.drawWallsAndGates(g, gridCells);
        
        // Finally draw items on top of everything
        itemManager.paintItems(g);
    }
    
    /**
     * Check if an animal can move from one cell to another
     */
    public boolean canMoveBetween(Cell from, Cell to, String animalType) {
        if (!initialized) {
            return true; // Fallback to normal movement if not initialized
        }
        
        return pathfinder.hasPath(from, to, animalType);
    }
    
    /**
     * Find a path for an animal from start to destination
     */
    public List<Cell> findPath(Cell start, Cell end, String animalType) {
        if (!initialized) {
            return null;
        }
        
        return pathfinder.findPath(start, end, animalType);
    }
    
    /**
     * Get information about a cell's zone
     */
    public String getCellInfo(Cell cell) {
        if (!initialized) {
            return "World not initialized";
        }
        
        for (Zone zone : zones) {
            if (zone.contains(cell)) {
                return "Zone " + zone.getZoneId() + " (" + zone.getLandscape() + ")";
            }
        }
        
        return "Unknown zone";
    }
    
    /**
     * Report that an animal failed to eat due to missing tools
     */
    public void reportToolFailure(String tool) {
        if (initialized) {
            spawnSystem.reportToolFailure(tool);
        }
    }
    
    /**
     * Report that an item was picked up
     */
    public void reportItemPickup(String itemType) {
        if (initialized) {
            spawnSystem.reportItemPickup(itemType);
        }
    }
    
    /**
     * Get all zones for debugging/testing
     */
    public List<Zone> getZones() {
        return zones;
    }
    
    /**
     * Get all spawn points for debugging/testing
     */
    public List<SpawnPoint> getSpawnPoints() {
        if (!initialized) {
            return new ArrayList<>();
        }
        return spawnManager.getAllSpawnPoints();
    }
    
    /**
     * Get stats about the world for debugging
     */
    public String getWorldStats() {
        if (!initialized) {
            return "World not initialized";
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("World Stats:\n");
        stats.append("Zones: ").append(zones.size()).append("\n");
        stats.append("Spawn Points: ").append(spawnManager.getAllSpawnPoints().size()).append("\n");
        stats.append("Actors: ").append(actors.size()).append("\n");
        
        // Count spawn points by landscape
        int legosPoints = 0, sandPoints = 0, depthsPoints = 0;
        for (SpawnPoint point : spawnManager.getAllSpawnPoints()) {
            switch (point.getZone().getLandscape()) {
                case LEGOS: legosPoints++; break;
                case SAND_DUNES: sandPoints++; break;
                case DEPTHS: depthsPoints++; break;
            }
        }
        
        stats.append("LEGOS spawn points: ").append(legosPoints).append("\n");
        stats.append("SAND spawn points: ").append(sandPoints).append("\n");
        stats.append("DEPTHS spawn points: ").append(depthsPoints).append("\n");
        
        return stats.toString();
    }
    
    /**
     * Get the item manager for item interactions
     */
    public ItemManager getItemManager() {
        return itemManager;
    }
}