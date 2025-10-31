package patterns.states;

import actors.Actor;

/**
 * PHASE 3: Hot Weather State - Animals seek shade and water
 */
public class HotWeatherState implements WeatherBehaviorState {
    
    @Override
    public void executeBehavior(Actor actor) {
        // Hot behavior - seek water/shade, pant, reduced activity
        
        // Animals lose water faster in heat
        if (Math.random() < 0.6) {
            actor.takeDamage(2); // Dehydration and heat stress
        }
        
        // Chance to find shade or water
        if (Math.random() < 0.3) {
            actor.heal(3); // Good recovery from finding relief
        }
        
        // Heat makes animals lethargic
        if (Math.random() < 0.4) {
            actor.takeDamage(1); // Additional lethargy damage
        }
    }
    
    @Override
    public boolean shouldRemainInState(boolean isRaining, double temperature, double windSpeed) {
        return !isRaining && temperature > 0.7; // Hot but not raining
    }
    
    @Override
    public WeatherBehaviorState getNextState(boolean isRaining, double temperature, double windSpeed) {
        if (isRaining) {
            return new ShelterSeekingState();
        }
        if (temperature >= 0.3 && temperature <= 0.7) {
            return new NormalWeatherState();
        }
        if (temperature < 0.3) {
            return new ColdWeatherState();
        }
        if (windSpeed >= 0.4) {
            return new WindyWeatherState();
        }
        return this; // Stay hot
    }
    
    @Override
    public String getStateDescription() {
        return "Seeking shade and water - Hot weather";
    }
    
    @Override
    public double getMovementSpeedModifier() {
        return 0.5; // Very slow in heat
    }
    
    @Override
    public double getHealthChangeRate() {
        return 1.6; // Fastest health decay in heat
    }
}