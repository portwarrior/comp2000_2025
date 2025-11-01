package world;

import actors.Actor;
import weather.SimpleWeatherManager;
import patterns.WeatherStrategyManager;
import patterns.states.WeatherStateManager;
import patterns.decorators.WeatherEffectManager;
import patterns.factories.WeatherItemFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Main coordinator that brings together zones, spawning, pathfinding, and rendering
 * Like a game director managing all the systems
 * PHASE 3: Enhanced with State, Factory, and Decorator patterns for advanced weather behavior
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
    
    //Weather system
    private SimpleWeatherManager weatherManager;
    private WeatherStrategyManager strategyManager;
    
    // Enhanced weather visualization systems
    private WeatherVisualEffects weatherEffects;
    private WeatherStatusDisplay statusDisplay;
    private WeatherNotificationSystem notificationSystem;
    
    // PHASE 3: Advanced Pattern Systems
    private WeatherStateManager stateManager;
    private WeatherEffectManager decoratorManager;
    // Factory pattern is static utility class - no field needed
    
    // Game state
    private List<Actor> actors;
    private java.util.Random random; // Shared random instance
    private long lastSpawnTick;
    private long lastHealthTick;
    private long lastWeatherUpdate;
    private boolean initialized;
    
    public GameWorldManager(Cell[][] gridCells, long worldSeed) {
        this.gridCells = gridCells;
        this.actors = new ArrayList<>();
        this.lastSpawnTick = 0;
        this.lastHealthTick = 0;
        this.lastWeatherUpdate = 0;
        this.initialized = false;
        this.random = new java.util.Random();
        
        // Initialize all systems with the same seed for consistency
        this.zoneGenerator = new SimpleZoneGenerator(worldSeed);
        this.spawnManager = new SpawnPointManager(worldSeed);
        this.renderer = new ZoneRenderer();
        this.itemManager = new ItemManager();
        
        // Initialize simple weather system
        this.weatherManager = new SimpleWeatherManager();
        this.strategyManager = new WeatherStrategyManager();
        
        // Initialize enhanced weather visualization systems
        this.weatherEffects = new WeatherVisualEffects();
        this.statusDisplay = new WeatherStatusDisplay(weatherManager, strategyManager);
        this.notificationSystem = new WeatherNotificationSystem();
        
        // PHASE 3: Initialize advanced pattern systems
        this.stateManager = new WeatherStateManager();
        this.decoratorManager = new WeatherEffectManager();
        
        // Notify that weather system is starting
        notificationSystem.notifyWeatherSystemEvent("PHASE 3 Weather System Activated - Advanced Patterns Online");
        
        initializeWorld();
    }
    
    /**
     * Set up the entire world with zones, spawn points, and pathfinding
     */
    private void initializeWorld() {
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("Initializing game world...");
        }
        
        // Step 1: Generate zones
        zones = zoneGenerator.generateZones(gridCells);
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("Generated " + zones.size() + " zones");
        }
        
        // Step 2: Create pathfinder
        pathfinder = new SimplePathfinder(gridCells, zones);
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("Pathfinding system ready");
        }
        
        // Step 3: Create spawn points
        spawnManager.createSpawnPoints(zones);
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("Created " + spawnManager.getAllSpawnPoints().size() + " spawn points");
        }
        
        // Step 4: Initialize spawn system
        spawnSystem = new GameifiedSpawnSystem(spawnManager, pathfinder, itemManager, System.currentTimeMillis());
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("Spawn system initialized");
        }
        
        // Step 5: Set up renderer
        renderer.initialize(zones);
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("Renderer ready");
        }
        
        initialized = true;
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("World initialization complete!");
        }
    }
    
    /**
     * Add an actor to the world
     * PHASE 3: Initialize with behavioral state management
     */
    public void addActor(Actor actor) {
        if (!actors.contains(actor)) {
            actors.add(actor);
            
            // PHASE 3: Initialize animal with state management
            stateManager.initializeAnimal(actor);
            
            if ("true".equals(System.getProperty("DEBUG_MODE"))) {
                System.out.println("Added " + actor.getClass().getSimpleName() + " to the world with state management");
            }
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
            
            // PHASE 3: Also run weather-based item spawning
            spawnWeatherItems();
            
            lastSpawnTick = currentTime;
        }
        
        // Update animal health every 5 seconds (hunger/thirst)
        if (currentTime - lastHealthTick > 5000) {
            updateAnimalHealth();
            lastHealthTick = currentTime;
        }
        
        // Update weather data every 30 seconds
        if (currentTime - lastWeatherUpdate > 30000) {
            updateWeatherData();
            lastWeatherUpdate = currentTime;
        }
    }
    
    /**
     * Update health for all animals (hunger, thirst, healing from items)
     */
    private void updateAnimalHealth() {
        for (Actor actor : actors) {
            if (actor.isAlive()) {
                // Animals lose 1-3 health per tick due to hunger/thirst
                int healthLoss = 1 + random.nextInt(3);
                
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
                    if ("true".equals(System.getProperty("DEBUG_MODE"))) {
                        System.out.println(actor.getClass().getSimpleName() + " is in critical condition!");
                    }
                }
            }
        }
    }
    
    /**
     * Try to heal an animal using items from their inventory
     */
    private void tryHealFromInventory(Actor actor) {
        // For now, just give a small heal if they have any food items
        // could expand this to use different items for different healing amounts
        if (!actor.getInventory().isEmpty()) {
            actor.heal(5); // Small healing from having food
        }
    }
    
    /**
     * Update weather data - simple version
     */
    private void updateWeatherData() {
        // Just tell the weather manager to update
        weatherManager.update();
        // Apply weather effects to animals
        applyWeatherEffects();
    }
    
    /**
     * Apply weather effects to animals using strategy pattern + PHASE 3 enhancements
     */
    private void applyWeatherEffects() {
        // Get comprehensive weather data for Phase 3
        weather.WeatherConditions conditions = weatherManager.getOverallWeather();
        decoratorManager.updateEffects(conditions);
        
        // Apply weather effects using strategy pattern for each animal type
        for (Actor actor : actors) {
            Cell actorCell = actor.getLocation();
            if (actorCell != null) {
                int x = actorCell.col - 'A';
                int y = actorCell.row;
                
                // Get weather conditions
                boolean isRaining = weatherManager.isRainingAt(x, y);
                double temperature = weatherManager.getTemperatureAt(x, y);
                double windSpeed = Math.sqrt(Math.pow(weatherManager.getWindXAt(x, y), 2) + 
                                           Math.pow(weatherManager.getWindYAt(x, y), 2));
                
                // PHASE 2: Use strategy pattern to apply animal-specific weather responses
                strategyManager.applyWeatherEffect(actor, isRaining, temperature);
                
                // PHASE 3: Update animal behavioral states based on weather
                stateManager.updateAnimalBehavior(actor, isRaining, temperature, windSpeed);
                
                // Get urgency level for notifications
                int urgency = strategyManager.getUrgencyLevel(actor, isRaining, temperature);
                
                // Check for health issues and generate notifications
                notificationSystem.checkAnimalWeatherHealth(actor, isRaining, temperature, urgency);
                
                // Get descriptive response for notifications
                patterns.WeatherStrategy strategy = strategyManager.getStrategyForAnimal(actor);
                String response = strategy.getWeatherResponse(isRaining, temperature);
                
                // Occasionally show what animals are doing (not spam)
                if (Math.random() < 0.03) { // 3% chance per update (reduced to add state info)
                    String stateInfo = stateManager.getAnimalStateDescription(actor);
                    notificationSystem.notifyWeatherResponse(actor, response + " (" + stateInfo + ")");
                }
            }
        }
    }
    
    /**
     * Render comprehensive weather visual effects using real server data
     */
    private void renderWeatherEffects(java.awt.Graphics2D g2d) {
        // Get comprehensive weather conditions from server data
        weather.WeatherConditions conditions = weatherManager.getOverallWeather();
        
        // Debug output for real weather data (only in debug mode)
        if ("true".equals(System.getProperty("DEBUG_MODE")) && Math.random() < 0.01) { // Occasional debug message
            System.out.println("DEBUG: " + conditions.toString());
            if (conditions.isRaining()) {
                System.out.println("DEBUG: Rain effects active! Intensity: " + conditions.rainIntensity);
            }
            if (conditions.isWindy()) {
                System.out.println("DEBUG: Wind active! Speed: " + String.format("%.2f", conditions.getWindSpeed()));
            }
        }
        
        // Add rain effects based on actual rain intensity
        if (conditions.isRaining()) {
            weatherEffects.addRainEffect(1024, 720, conditions.rainIntensity);
        }
        
        // Add wind effects using real wind data
        if (conditions.isWindy()) {
            weatherEffects.addWindEffect(1024, 720, conditions.windX, conditions.windY, conditions.getWindSpeed());
        }
        
        // Render all weather effects with comprehensive real data including wind direction
        weatherEffects.render(g2d, 1024, 720, conditions.isRaining(), conditions.temperature, 
                            conditions.windX, conditions.windY, conditions.getWindSpeed());
    }
    
    /**
     * Render weather UI elements
     */
    private void renderWeatherUI(java.awt.Graphics2D g2d) {
        // Render weather status panel
        statusDisplay.renderWeatherStatus(g2d, 1024, 720);
        
        // Render notifications
        notificationSystem.render(g2d, 1024);
        
        // Render individual animal status for each actor
        for (Actor actor : actors) {
            Cell location = actor.getLocation();
            if (location != null) {
                // Convert cell position to screen coordinates
                int screenX = (location.col - 'A') * 40 + 50; // Adjusted for better positioning
                int screenY = location.row * 40 + 50;
                
                statusDisplay.renderAnimalStatus(g2d, actor, screenX, screenY);
            }
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
     * Render weather overlay effects on top of everything
     */
    public void renderWeatherOverlay(java.awt.Graphics2D g2d) {
        if (!initialized) {
            return;
        }
        
        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                           java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Add weather visual effects (constrained to grid area only)
        renderWeatherEffects(g2d);
        
        // Render weather status and notifications on top
        renderWeatherUI(g2d);
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
     * PHASE 3: Spawn weather-based items using Factory Pattern
     */
    private void spawnWeatherItems() {
        // Get current weather to influence spawn rates
        weather.WeatherConditions conditions = weatherManager.getOverallWeather();
        
        // Use WeatherItemFactory to get weather-appropriate spawn rate
        double baseSpawnChance = 0.15; // 15% per spawn tick
        double weatherMultiplier = WeatherItemFactory.getWeatherSpawnRateMultiplier(conditions);
        double finalSpawnChance = Math.min(baseSpawnChance * weatherMultiplier, 0.3); // Cap at 30%
        
        if (random.nextDouble() < finalSpawnChance) {
            // Try to spawn weather-appropriate items
            spawnWeatherAppropriateItem(conditions);
        }
        
        // Emergency weather items (lower chance but important)
        if (random.nextDouble() < 0.05) { // 5% chance for emergency items
            spawnEmergencyWeatherItem(conditions);
        }
    }

    /**
     * PHASE 3: Spawn weather-appropriate items using Factory Pattern
     */
    private void spawnWeatherAppropriateItem(weather.WeatherConditions conditions) {
        // Find a suitable spawn location
        SpawnPoint randomSpawn = getRandomSpawnPoint();
        if (randomSpawn != null && zones != null && !zones.isEmpty()) {
            // Get landscape type for the spawn location
            world.LandscapeType landscape = world.LandscapeType.NEUTRAL; // Default
            for (Zone zone : zones) {
                if (zone.contains(randomSpawn.getCell())) {
                    landscape = zone.getLandscape();
                    break;
                }
            }
            
            // Use factory to create weather-appropriate consumable
            items.consumables.Consumable weatherItem = WeatherItemFactory.createWeatherConsumable(conditions, landscape);
            if (weatherItem != null) {
                // Get item type name and spawn using ItemManager interface
                String itemType = weatherItem.getClass().getSimpleName();
                itemManager.spawnItem(itemType, randomSpawn.getCell());
            }
        }
    }
    
    /**
     * PHASE 3: Spawn emergency weather items for extreme conditions
     */
    private void spawnEmergencyWeatherItem(weather.WeatherConditions conditions) {
        // Only spawn emergency items during extreme weather
        boolean extremeConditions = conditions.rainIntensity > 0.7 || 
                                  conditions.temperature > 0.8 || 
                                  conditions.temperature < 0.2 ||
                                  conditions.getWindSpeed() > 0.6;
        
        if (extremeConditions) {
            SpawnPoint randomSpawn = getRandomSpawnPoint();
            if (randomSpawn != null) {
                items.consumables.Consumable emergencyItem = WeatherItemFactory.createEmergencyWeatherItem(conditions);
                if (emergencyItem != null) {
                    // Get item type name and spawn using ItemManager interface
                    String itemType = emergencyItem.getClass().getSimpleName();
                    itemManager.spawnItem(itemType, randomSpawn.getCell());
                    
                    // Notify about emergency item spawn
                    notificationSystem.addNotification("Emergency " + itemType + " spawned due to extreme weather!", 
                                                     WeatherNotificationSystem.NotificationType.SUCCESS, 3);
                }
            }
        }
    }
    
    /**
     * Helper method to get random spawn point
     */
    private SpawnPoint getRandomSpawnPoint() {
        if (spawnManager != null && !spawnManager.getAllSpawnPoints().isEmpty()) {
            java.util.List<SpawnPoint> spawnPoints = spawnManager.getAllSpawnPoints();
            int randomIndex = random.nextInt(spawnPoints.size());
            return spawnPoints.get(randomIndex);
        }
        return null;
    }
    
    /**
     * Get the item manager for item interactions
     */
    public ItemManager getItemManager() {
        return itemManager;
    }
    
    /**
     * Simple weather methods for the game
     */
    public boolean isRainingAt(int x, int y) {
        return weatherManager.isRainingAt(x, y);
    }
    
    public double getTemperatureAt(int x, int y) {
        return weatherManager.getTemperatureAt(x, y);
    }
    
    public String getWeatherSummary() {
        return weatherManager.getWeatherSummary();
    }
}