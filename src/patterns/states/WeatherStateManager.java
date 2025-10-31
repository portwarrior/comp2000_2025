package patterns.states;

import actors.Actor;
import java.util.HashMap;
import java.util.Map;

/**
 * PHASE 3: Weather State Manager
 * Manages behavioral state transitions for animals based on weather
 */
public class WeatherStateManager {
    private Map<Actor, WeatherBehaviorState> animalStates;
    
    public WeatherStateManager() {
        this.animalStates = new HashMap<>();
    }
    
    /**
     * Initialize an animal with default state
     */
    public void initializeAnimal(Actor actor) {
        animalStates.put(actor, new NormalWeatherState());
    }
    
    /**
     * Update animal behavior based on current weather conditions
     */
    public void updateAnimalBehavior(Actor actor, boolean isRaining, double temperature, double windSpeed) {
        WeatherBehaviorState currentState = animalStates.get(actor);
        
        if (currentState == null) {
            initializeAnimal(actor);
            currentState = animalStates.get(actor);
        }
        
        // Execute current state behavior
        currentState.executeBehavior(actor);
        
        // Check if state should transition
        if (!currentState.shouldRemainInState(isRaining, temperature, windSpeed)) {
            WeatherBehaviorState newState = currentState.getNextState(isRaining, temperature, windSpeed);
            animalStates.put(actor, newState);
            
            // Notify about state change (only in debug mode)
            if ("true".equals(System.getProperty("DEBUG_MODE"))) {
                System.out.println(actor.getClass().getSimpleName() + " changed state: " + 
                                 newState.getStateDescription());
            }
        }
    }
    
    /**
     * Get current state description for an animal
     */
    public String getAnimalStateDescription(Actor actor) {
        WeatherBehaviorState state = animalStates.get(actor);
        return state != null ? state.getStateDescription() : "Unknown state";
    }
    
    /**
     * Get movement speed modifier for an animal based on current state
     */
    public double getMovementSpeedModifier(Actor actor) {
        WeatherBehaviorState state = animalStates.get(actor);
        return state != null ? state.getMovementSpeedModifier() : 1.0;
    }
    
    /**
     * Get health change rate modifier for an animal based on current state
     */
    public double getHealthChangeRateModifier(Actor actor) {
        WeatherBehaviorState state = animalStates.get(actor);
        return state != null ? state.getHealthChangeRate() : 1.0;
    }
    
    /**
     * Get statistics about current states
     */
    public Map<String, Integer> getStateStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        for (WeatherBehaviorState state : animalStates.values()) {
            String stateName = state.getClass().getSimpleName();
            stats.put(stateName, stats.getOrDefault(stateName, 0) + 1);
        }
        
        return stats;
    }
}