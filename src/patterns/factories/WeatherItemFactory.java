package patterns.factories;

import items.consumables.*;
import world.LandscapeType;
import weather.WeatherConditions;

/**
 * PHASE 3: Weather Item Factory
 * Dynamically creates weather-appropriate items based on conditions
 */
public class WeatherItemFactory {
    
    // Private constructor to prevent instantiation
    private WeatherItemFactory() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Create weather-appropriate consumable items
     */
    public static Consumable createWeatherConsumable(WeatherConditions weather, LandscapeType landscape) {
        
        // Rain-specific items
        if (weather.isRaining()) {
            // Rain makes water more available and animals need hydration
            return new Water();
        }
        
        // Cold weather items
        if (weather.isCold()) {
            // Cold weather increases caloric needs
            if (landscape == LandscapeType.LEGOS) {
                return new Bone(); // High energy bones for cold weather
            } else if (landscape == LandscapeType.SAND_DUNES) {
                return new Milk(); // Warming milk for cold conditions
            } else {
                return new Bone(); // Default high-energy option
            }
        }
        
        // Hot weather items
        if (weather.isHot()) {
            // Hot weather prioritizes hydration
            return new Water(); // Essential for hot weather
        }
        
        // Windy weather items
        if (weather.isWindy()) {
            // Wind can carry different scents and opportunities
            if (landscape == LandscapeType.NEUTRAL) {
                return new Worm(); // Wind brings worms to surface
            } else {
                return new Water(); // Wind increases dehydration
            }
        }
        
        // Default weather-neutral items based on landscape
        return createLandscapeAppropriateItem(landscape);
    }
    
    /**
     * Create landscape-appropriate items when weather is neutral
     */
    private static Consumable createLandscapeAppropriateItem(LandscapeType landscape) {
        switch (landscape) {
            case LEGOS:
                return Math.random() < 0.6 ? new Bone() : new Water();
            case SAND_DUNES:
                return Math.random() < 0.5 ? new Milk() : new Water();
            case DEPTHS:
                return new Water(); // Water zone always has water
            case NEUTRAL:
            default:
                // Balanced distribution for neutral areas
                double rand = Math.random();
                if (rand < 0.3) return new Bone();
                if (rand < 0.6) return new Milk();
                if (rand < 0.8) return new Water();
                return new Worm();
        }
    }
    

    
    /**
     * Get weather-specific spawn rate multiplier
     */
    public static double getWeatherSpawnRateMultiplier(WeatherConditions weather) {
        double multiplier = 1.0;
        
        // Rain increases water spawn rate
        if (weather.isRaining()) {
            multiplier *= (1.0 + weather.rainIntensity * 0.5); // Up to 50% more in heavy rain
        }
        
        // Cold weather increases food spawn rate (animals need more energy)
        if (weather.isCold()) {
            multiplier *= 1.3; // 30% more food in cold
        }
        
        // Hot weather increases water spawn rate
        if (weather.isHot()) {
            multiplier *= 1.4; // 40% more water in hot weather
        }
        
        // Wind can increase or decrease spawn rates randomly
        if (weather.isWindy()) {
            multiplier *= (0.8 + Math.random() * 0.4); // 80% to 120% - wind is unpredictable
        }
        
        return Math.max(0.5, Math.min(2.0, multiplier)); // Clamp between 50% and 200%
    }
    
    /**
     * Create emergency weather items when animals are in critical condition
     */
    public static Consumable createEmergencyWeatherItem(WeatherConditions weather) {
        if (weather.isHot()) {
            return new Water(); // Emergency hydration in heat
        }
        if (weather.isCold()) {
            return new Bone(); // Emergency high-energy food in cold
        }
        if (weather.isRaining()) {
            return new Water(); // Take advantage of rain for water collection
        }
        
        return new Water(); // Water is always a safe emergency item
    }
}