package patterns.states;

import actors.Actor;

/**
 * PHASE 3: Normal Weather State - Default behavior
 * Animals behave normally in mild weather conditions
 */
public class NormalWeatherState implements WeatherBehaviorState {
    
    @Override
    public void executeBehavior(Actor actor) {
        // Normal behavior - standard movement, normal health decay
        // Animals seek food and water at normal rates
        
        // Small chance to move randomly
        if (Math.random() < 0.3) {
            // Normal exploration behavior
            actor.heal(1); // Slight health recovery from comfortable conditions
        }
    }
    
    @Override
    public boolean shouldRemainInState(boolean isRaining, double temperature, double windSpeed) {
        // Stay in normal state if weather is mild
        return !isRaining && 
               temperature >= 0.3 && temperature <= 0.7 && 
               windSpeed < 0.4;
    }
    
    @Override
    public WeatherBehaviorState getNextState(boolean isRaining, double temperature, double windSpeed) {
        if (isRaining) {
            return new ShelterSeekingState();
        }
        if (temperature < 0.3) {
            return new ColdWeatherState();
        }
        if (temperature > 0.7) {
            return new HotWeatherState();
        }
        if (windSpeed >= 0.4) {
            return new WindyWeatherState();
        }
        return this; // Stay in current state
    }
    
    @Override
    public String getStateDescription() {
        return "Comfortable - Normal behavior";
    }
    
    @Override
    public double getMovementSpeedModifier() {
        return 1.0; // Normal speed
    }
    
    @Override
    public double getHealthChangeRate() {
        return 1.0; // Normal health decay rate
    }
}