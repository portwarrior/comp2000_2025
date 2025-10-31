package patterns;

import actors.Actor;
import actors.Cat;
import actors.Dog;
import actors.Bird;

/**
 * Simple manager to decide which weather strategy to use for each animal
 * Student-level implementation
 */
public class WeatherStrategyManager {
    private WeatherStrategy catStrategy;
    private WeatherStrategy dogStrategy;
    private WeatherStrategy birdStrategy;
    
    public WeatherStrategyManager() {
        catStrategy = new SimpleWeatherStrategy("Cat");
        dogStrategy = new SimpleWeatherStrategy("Dog");
        birdStrategy = new SimpleWeatherStrategy("Bird");
    }
    
    // Figure out which strategy to use for this animal
    public WeatherStrategy getStrategyForAnimal(Actor animal) {
        if (animal instanceof Cat) {
            return catStrategy;
        } else if (animal instanceof Dog) {
            return dogStrategy;
        } else if (animal instanceof Bird) {
            return birdStrategy;
        } else {
            // Default strategy for unknown animals
            return catStrategy; // Use cat strategy as default
        }
    }
    
    /**
     * Apply weather effects to an actor using appropriate strategy
     */
    public void applyWeatherEffect(Actor actor, boolean isRaining, double temperature) {
        WeatherStrategy strategy = getStrategyForAnimal(actor);
        strategy.respondToWeather(actor, isRaining, temperature);
    }
    
    /**
     * Get urgency level for an actor based on weather conditions
     */
    public int getUrgencyLevel(Actor actor, boolean isRaining, double temperature) {
        WeatherStrategy strategy = getStrategyForAnimal(actor);
        return strategy.getUrgencyLevel(isRaining, temperature);
    }
    
    // Test method to show different strategies
    public void demonstrateStrategies(boolean isRaining, double temperature) {
        System.out.println("=== Weather Strategy Demo ===");
        System.out.println("Rain: " + isRaining + ", Temperature: " + temperature);
        
        System.out.println("Cat urgency: " + catStrategy.getUrgencyLevel(isRaining, temperature));
        System.out.println("Dog urgency: " + dogStrategy.getUrgencyLevel(isRaining, temperature));
        System.out.println("Bird urgency: " + birdStrategy.getUrgencyLevel(isRaining, temperature));
        System.out.println();
    }
}