package world;

import actors.Actor;
import items.consumables.Consumable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Advanced spawn system with health bias, distance factors, and pity systems
 * Like a smart game director that makes sure the game is fun and fair
 */
public class GameifiedSpawnSystem {
    
    private SpawnPointManager spawnManager;
    private SimplePathfinder pathfinder;
    private Random random;
    private ItemManager itemManager;
    
    // Spawn weights for different landscapes
    private Map<String, Map<String, Double>> landscapeWeights;
    
    // Pity counters to ensure fairness
    private Map<String, Long> lastItemPickupTime;
    private Map<String, Integer> toolFailureCount;
    private Map<String, Integer> gearFailureCount;
    
    // Active items tracking
    private Map<Zone, Consumable> activeConsumables;
    private Map<Zone, List<String>> activeTools;
    
    // Event system
    private String currentEvent;
    private long eventEndTime;
    
    public GameifiedSpawnSystem(SpawnPointManager spawnManager, SimplePathfinder pathfinder, ItemManager itemManager, long seed) {
        this.spawnManager = spawnManager;
        this.pathfinder = pathfinder;
        this.itemManager = itemManager;
        this.random = new Random(seed);
        
        this.landscapeWeights = new HashMap<>();
        this.lastItemPickupTime = new HashMap<>();
        this.toolFailureCount = new HashMap<>();
        this.gearFailureCount = new HashMap<>();
        this.activeConsumables = new HashMap<>();
        this.activeTools = new HashMap<>();
        
        initializeWeights();
    }
    
    /**
     * Set up base spawn weights for each landscape type
     */
    private void initializeWeights() {
        // NEUTRAL weights (default zone - all animals allowed, balanced spawning)
        Map<String, Double> neutralWeights = new HashMap<>();
        neutralWeights.put("bone", 0.3);     // Dog food
        neutralWeights.put("milk", 0.3);     // Cat food
        neutralWeights.put("worm", 0.3);     // Bird food
        neutralWeights.put("water", 0.5);    // Universal need
        neutralWeights.put("bowl", 0.15);    // Tools
        neutralWeights.put("cup", 0.15);
        neutralWeights.put("saucer", 0.15);
        landscapeWeights.put("NEUTRAL", neutralWeights);
        
        Map<String, Double> legosWeights = new HashMap<>();
        legosWeights.put("bone", 2.0);     // Super high dog food priority
        legosWeights.put("water", 0.3);    // Reduce water priority
        legosWeights.put("bowl", 0.4);     // Dog tools
        legosWeights.put("milk", 0.1);     // Less cat food
        legosWeights.put("worm", 0.1);     // Less bird food
        legosWeights.put("cup", 0.1);
        legosWeights.put("saucer", 0.1);
        landscapeWeights.put("LEGOS", legosWeights);
        
        // SAND_DUNES weights (cat heaven)
        Map<String, Double> sandWeights = new HashMap<>();
        sandWeights.put("milk", 0.8);      // High cat food
        sandWeights.put("water", 0.4);     // Cats need water too
        sandWeights.put("cup", 0.3);       // Cat tools
        sandWeights.put("saucer", 0.3);
        landscapeWeights.put("SAND_DUNES", sandWeights);
        
        // DEPTHS weights (water zone - birds only)
        Map<String, Double> depthsWeights = new HashMap<>();
        depthsWeights.put("water", 0.9);   // Lots of water
        depthsWeights.put("worm", 0.7);    // Bird food in water
        depthsWeights.put("saucer", 0.2);  // Bird tools
        landscapeWeights.put("DEPTHS", depthsWeights);
    }
    
    /**
     * Main spawn tick - called every second or so
     */
    public void spawnTick(List<Zone> zones, List<Actor> animals) {
        updateCurrentEvent();
        
        for (Zone zone : zones) {
            trySpawnInZone(zone, animals);
        }
        
        // Check if we need pity spawns
        checkPitySpawns(zones, animals);
    }
    
    /**
     * Try to spawn an item in a specific zone
     */
    private void trySpawnInZone(Zone zone, List<Actor> animals) {
        // Rule: Only one consumable per zone at a time
        if (activeConsumables.containsKey(zone)) {
            return; // Zone already has a consumable
        }
        
        List<SpawnPoint> availablePoints = spawnManager.getSpawnPointsInZone(zone);
        List<SpawnPoint> readyPoints = new ArrayList<>();
        
        // Filter to points that are ready to spawn
        for (SpawnPoint point : availablePoints) {
            if (point.isReadyToSpawn()) {
                readyPoints.add(point);
            }
        }
        
        if (readyPoints.isEmpty()) {
            return; // No points ready
        }
        
        // Calculate what should spawn and where
        SpawnDecision decision = calculateBestSpawn(zone, animals, readyPoints);
        
        if (decision != null && decision.weight > 0.05) { // Lower threshold for more variety
            executeSpawn(decision);
        }
    }
    
    /**
     * Calculate the best item to spawn and where
     */
    private SpawnDecision calculateBestSpawn(Zone zone, List<Actor> animals, List<SpawnPoint> spawnPoints) {
        String landscape = zone.getLandscape().toString();
        Map<String, Double> baseWeights = landscapeWeights.get(landscape);
        
        if (baseWeights == null) {
            return null;
        }
        
        SpawnDecision bestDecision = null;
        double bestWeight = 0;
        
        // Try each possible item type
        for (String itemType : baseWeights.keySet()) {
            for (SpawnPoint spawnPoint : spawnPoints) {
                
                if (!spawnPoint.canSpawn(itemType)) {
                    continue;
                }
                
                double weight = calculateSpawnWeight(itemType, spawnPoint, zone, animals);
                
                // Debug: Show weight calculations
                if (weight > 0.01) {
                    System.out.println("DEBUG: " + itemType + " weight " + String.format("%.3f", weight) + 
                                     " in " + landscape + " zone");
                }
                
                if (weight > bestWeight) {
                    bestWeight = weight;
                    bestDecision = new SpawnDecision(itemType, spawnPoint, weight);
                }
            }
        }
        
        return bestDecision;
    }
    
    /**
     * Calculate the final spawn weight for an item at a specific point
     */
    private double calculateSpawnWeight(String itemType, SpawnPoint spawnPoint, Zone zone, List<Actor> animals) {
        String landscape = zone.getLandscape().toString();
        Map<String, Double> baseWeights = landscapeWeights.get(landscape);
        
        double baseWeight = baseWeights.getOrDefault(itemType, 0.0);
        
        if (baseWeight == 0) {
            return 0; // Item not allowed in this landscape
        }
        
        // Apply various multipliers
        double healthMultiplier = calculateHealthBias(itemType, animals, spawnPoint);
        double distanceMultiplier = calculateDistanceFactor(itemType, spawnPoint, animals);
        double scarcityMultiplier = calculateScarcityBonus(itemType);
        double reachabilityMultiplier = calculateReachability(itemType, spawnPoint, animals);
        double eventMultiplier = calculateEventBonus(itemType, zone);
        double toolMultiplier = itemType.contains("bowl") || itemType.contains("cup") || itemType.contains("saucer") ? 0.7 : 1.0;
        
        double finalWeight = baseWeight * healthMultiplier * distanceMultiplier * 
                           scarcityMultiplier * reachabilityMultiplier * eventMultiplier * toolMultiplier;
        
        // Water safety floor
        if ("water".equals(itemType)) {
            finalWeight = Math.max(finalWeight, 0.15);
        }
        
        return finalWeight;
    }
    
    /**
     * Boost spawn weight if animals need this item urgently
     */
    private double calculateHealthBias(String itemType, List<Actor> animals, SpawnPoint spawnPoint) {
        // Simple approach: if any animal's health is low, boost their required food
        for (Actor animal : animals) {
            String animalType = animal.getClass().getSimpleName();
            
            // Check if this animal needs this item
            boolean needsItem = false;
            if ("Dog".equals(animalType) && "bone".equals(itemType)) {
                needsItem = true;
            } else if ("Cat".equals(animalType) && "milk".equals(itemType)) {
                needsItem = true;
            } else if ("Bird".equals(animalType) && "worm".equals(itemType)) {
                needsItem = true;
            } else if ("water".equals(itemType)) {
                needsItem = true; // All animals need water
            }
            
            if (needsItem) {
                // Check if animal can reach this spawn point
                if (pathfinder.hasPath(animal.getLocation(), spawnPoint.getCell(), animalType)) {
                    // Simple health check (assume animals start at 100% health)
                    // In real game, you'd check actual health values
                    return 1.75; // Health bias multiplier
                }
            }
        }
        
        return 1.0; // No health bias
    }
    
    /**
     * Prefer spawning at medium distance from animals (not too easy, not too hard)
     */
    private double calculateDistanceFactor(String itemType, SpawnPoint spawnPoint, List<Actor> animals) {
        if (animals.isEmpty()) {
            return 1.0;
        }
        
        int totalDistance = 0;
        int reachableAnimals = 0;
        
        for (Actor animal : animals) {
            if (pathfinder.hasPath(animal.getLocation(), spawnPoint.getCell(), animal.getClass().getSimpleName())) {
                totalDistance += spawnPoint.getDistanceTo(animal.getLocation());
                reachableAnimals++;
            }
        }
        
        if (reachableAnimals == 0) {
            return 0; // No animals can reach this point
        }
        
        double avgDistance = (double) totalDistance / reachableAnimals;
        
        // Prefer distances between 6-9 cells (sweet spot for exploration)
        if (avgDistance >= 6 && avgDistance <= 9) {
            return 1.5; // Sweet spot bonus
        } else if (avgDistance >= 3 && avgDistance <= 12) {
            return 1.0; // Acceptable range
        } else if (avgDistance < 3) {
            return 0.7; // Too close - too easy
        } else {
            return 0.5; // Too far - too frustrating
        }
    }
    
    /**
     * Boost items that haven't been seen in a while
     */
    private double calculateScarcityBonus(String itemType) {
        Long lastSeen = lastItemPickupTime.get(itemType);
        
        if (lastSeen == null) {
            return 1.2; // First time seeing this item
        }
        
        long timeSince = System.currentTimeMillis() - lastSeen;
        long minutes = timeSince / (60 * 1000);
        
        if (minutes > 3) {
            return 1.4; // Been a while, boost it
        } else if (minutes > 1) {
            return 1.1; // Slight boost
        } else {
            return 0.8; // Recently seen, reduce chance
        }
    }
    
    /**
     * Only spawn items where animals can actually reach them
     */
    private double calculateReachability(String itemType, SpawnPoint spawnPoint, List<Actor> animals) {
        // Check if any animal that needs this item can reach the spawn point
        for (Actor animal : animals) {
            String animalType = animal.getClass().getSimpleName();
            
            // Check if this animal would want this item
            boolean wantsItem = false;
            if ("Dog".equals(animalType) && ("bone".equals(itemType) || "bowl".equals(itemType) || "water".equals(itemType))) {
                wantsItem = true;
            } else if ("Cat".equals(animalType) && ("milk".equals(itemType) || "cup".equals(itemType) || "water".equals(itemType))) {
                wantsItem = true;
            } else if ("Bird".equals(animalType) && ("worm".equals(itemType) || "water".equals(itemType))) {
                wantsItem = true;
            }
            
            if (wantsItem && pathfinder.hasPath(animal.getLocation(), spawnPoint.getCell(), animalType)) {
                return 1.0; // At least one interested animal can reach it
            }
        }
        
        return 0; // No interested animals can reach this point
    }
    
    /**
     * Apply event-based bonuses
     */
    private double calculateEventBonus(String itemType, Zone zone) {
        if (currentEvent == null) {
            return 1.0;
        }
        
        switch (currentEvent) {
            case "BRICK_BONANZA":
                if (zone.getLandscape() == LandscapeType.LEGOS && "bone".equals(itemType)) {
                    return 2.0; // Double bone spawns in LEGOS during event
                }
                break;
                
            case "DUNE_GUST":
                if (zone.getLandscape() == LandscapeType.SAND_DUNES && 
                    ("milk".equals(itemType) || "water".equals(itemType))) {
                    return 1.5; // Boost milk and water in sand during event
                }
                break;
                
            case "COLD_CURRENT":
                if (zone.getLandscape() == LandscapeType.DEPTHS && "saucer".equals(itemType)) {
                    return 1.3; // Boost gear drops in depths during event
                }
                break;
        }
        
        return 1.0;
    }
    
    /**
     * Execute the spawn decision
     */
    private void executeSpawn(SpawnDecision decision) {
        // Use ItemManager to spawn the visual item
        itemManager.spawnItem(decision.itemType, decision.spawnPoint.getCell());
        
        // Set cooldown on the spawn point
        decision.spawnPoint.setCooldown(20000); // 20 second cooldown
        decision.spawnPoint.setLastSpawnedItem(decision.itemType);
        
        // Track active items
        if (decision.itemType.equals("bone") || decision.itemType.equals("milk") || 
            decision.itemType.equals("worm") || decision.itemType.equals("water")) {
            // This is a consumable - track it
            // activeConsumables.put(decision.spawnPoint.getZone(), consumableItem);
        }
    }
    
    /**
     * Check for pity spawns when things are going badly
     */
    private void checkPitySpawns(List<Zone> zones, List<Actor> animals) {
        checkWaterPity(zones, animals);
        checkToolPity(zones, animals);
    }
    
    /**
     * Ensure there's always water available when animals are thirsty
     */
    private void checkWaterPity(List<Zone> zones, List<Actor> animals) {
        Long lastWater = lastItemPickupTime.get("water");
        
        if (lastWater == null || (System.currentTimeMillis() - lastWater) > 60000) { // 60 seconds
            // It's been too long since water was picked up - force spawn one
            
            for (Zone zone : zones) {
                List<SpawnPoint> points = spawnManager.getSpawnPointsInZone(zone);
                
                for (SpawnPoint point : points) {
                    if (point.canSpawn("water") && point.isReadyToSpawn()) {
                        SpawnDecision pitySpawn = new SpawnDecision("water", point, 1.0);
                        executeSpawn(pitySpawn);
                        System.out.println("PITY SPAWN: Emergency water for thirsty animals!");
                        return; // Only spawn one
                    }
                }
            }
        }
    }
    
    /**
     * Boost tool spawns when animals keep failing to eat due to missing bowls/cups
     */
    private void checkToolPity(List<Zone> zones, List<Actor> animals) {
        for (String tool : new String[]{"bowl", "cup", "saucer"}) {
            Integer failures = toolFailureCount.get(tool);
            
            if (failures != null && failures >= 2) {
                // Animal has failed to eat 2+ times due to missing this tool
                // Boost tool spawn rates temporarily
                
                System.out.println("PITY SYSTEM: Boosting " + tool + " spawns due to repeated failures");
                
                // Reset the failure counter
                toolFailureCount.put(tool, 0);
                
                // Try to spawn the needed tool
                for (Zone zone : zones) {
                    List<SpawnPoint> points = spawnManager.getSpawnPointsInZone(zone);
                    
                    for (SpawnPoint point : points) {
                        if (point.canSpawn(tool) && point.isReadyToSpawn()) {
                            SpawnDecision pitySpawn = new SpawnDecision(tool, point, 1.0);
                            executeSpawn(pitySpawn);
                            return;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Update current special event
     */
    private void updateCurrentEvent() {
        if (currentEvent != null && System.currentTimeMillis() > eventEndTime) {
            System.out.println("Event ended: " + currentEvent);
            currentEvent = null;
        }
        
        // 5% chance to start a new event each tick (if no event active)
        if (currentEvent == null && random.nextDouble() < 0.05) {
            startRandomEvent();
        }
    }
    
    /**
     * Start a random special event
     */
    private void startRandomEvent() {
        String[] events = {"BRICK_BONANZA", "DUNE_GUST", "COLD_CURRENT"};
        currentEvent = events[random.nextInt(events.length)];
        
        // Events last 30-60 seconds
        int duration = 30000 + random.nextInt(30000);
        eventEndTime = System.currentTimeMillis() + duration;
        
        System.out.println("Special event started: " + currentEvent + " (lasting " + duration/1000 + " seconds)");
    }
    
    /**
     * Call this when an animal fails to eat due to missing tools
     */
    public void reportToolFailure(String tool) {
        toolFailureCount.put(tool, toolFailureCount.getOrDefault(tool, 0) + 1);
    }
    
    /**
     * Call this when an item is picked up
     */
    public void reportItemPickup(String itemType) {
        lastItemPickupTime.put(itemType, System.currentTimeMillis());
    }
    
    /**
     * Internal class to represent a spawn decision
     */
    private static class SpawnDecision {
        String itemType;
        SpawnPoint spawnPoint;
        double weight;
        
        SpawnDecision(String itemType, SpawnPoint spawnPoint, double weight) {
            this.itemType = itemType;
            this.spawnPoint = spawnPoint;
            this.weight = weight;
        }
    }
}