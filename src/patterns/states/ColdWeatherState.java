package patterns.states;

import actors.Actor;

/**
 * PHASE 3: Cold Weather State - Animals conserve energy and seek warmth
 */
public class ColdWeatherState implements WeatherBehaviorState {
    
    @Override
    public void executeBehavior(Actor actor) {
        // Cold behavior - conserve energy, seek food more actively, slower movement
        
        // Animals need more food in cold weather
        if (Math.random() < 0.5) {
            actor.takeDamage(2); // Higher energy consumption to stay warm
        }
        
        // Occasionally find warmth or conserve energy successfully
        if (Math.random() < 0.2) {
            actor.heal(1); // Small recovery from successful warmth conservation
        }
    }
    
    @Override
    public boolean shouldRemainInState(boolean isRaining, double temperature, double windSpeed) {
        return !isRaining && temperature < 0.3; // Cold but not raining
    }
    
    @Override
    public WeatherBehaviorState getNextState(boolean isRaining, double temperature, double windSpeed) {
        if (isRaining) {
            return new ShelterSeekingState();
        }
        if (temperature >= 0.3 && temperature <= 0.7) {
            return new NormalWeatherState();
        }
        if (temperature > 0.7) {
            return new HotWeatherState();
        }
        if (windSpeed >= 0.4) {
            return new WindyWeatherState();
        }
        return this; // Stay cold
    }
    
    @Override
    public String getStateDescription() {
        return "Conserving energy - Cold weather";
    }
    
    @Override
    public double getMovementSpeedModifier() {
        return 0.7; // Slower in cold
    }
    
    @Override
    public double getHealthChangeRate() {
        return 1.4; // Faster health decay in cold
    }
}