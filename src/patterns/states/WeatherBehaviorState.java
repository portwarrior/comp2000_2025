package patterns.states;

import actors.Actor;

/**
 * PHASE 3: State Pattern Implementation
 * Animals have different behavioral states based on weather conditions
 */
public interface WeatherBehaviorState {
    
    /**
     * Execute behavior in this state
     */
    void executeBehavior(Actor actor);
    
    /**
     * Check if the weather conditions warrant staying in this state
     */
    boolean shouldRemainInState(boolean isRaining, double temperature, double windSpeed);
    
    /**
     * Get the next appropriate state based on weather conditions
     */
    WeatherBehaviorState getNextState(boolean isRaining, double temperature, double windSpeed);
    
    /**
     * Get human-readable description of this state
     */
    String getStateDescription();
    
    /**
     * Get state-specific movement speed modifier
     */
    double getMovementSpeedModifier();
    
    /**
     * Get state-specific health change rate
     */
    double getHealthChangeRate();
}