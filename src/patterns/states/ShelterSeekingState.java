package patterns.states;

import actors.Actor;

/**
 * PHASE 3: Shelter Seeking State - Animals seek shelter during rain
 */
public class ShelterSeekingState implements WeatherBehaviorState {
    
    @Override
    public void executeBehavior(Actor actor) {
        // Rain behavior - seek shelter, move less, consume more energy
        
        // Higher chance to stay in place (seeking shelter)
        if (Math.random() < 0.7) {
            // Take slight damage from rain exposure if no shelter found
            actor.takeDamage(1);
        } else {
            // Successfully found some shelter - small health boost
            actor.heal(2);
        }
        
        // Animals are more stressed and eat more when seeking shelter
        // note to self: maybe make this dependent on animal type later?
        if (Math.random() < 0.4) {
            // Stress eating behavior
            actor.takeDamage(1); // Increased energy consumption from stress
        }
    }
    
    @Override
    public boolean shouldRemainInState(boolean isRaining, double temperature, double windSpeed) {
        return isRaining; // Stay in this state while it's raining
    }
    
    @Override
    public WeatherBehaviorState getNextState(boolean isRaining, double temperature, double windSpeed) {
        if (!isRaining) {
            // Rain stopped - check other conditions
            if (temperature < 0.3) {
                return new ColdWeatherState();
            }
            if (temperature > 0.7) {
                return new HotWeatherState();
            }
            if (windSpeed >= 0.4) {
                return new WindyWeatherState();
            }
            return new NormalWeatherState();
        }
        return this; // Continue seeking shelter while raining
    }
    
    @Override
    public String getStateDescription() {
        return "Seeking shelter from rain";
    }
    
    @Override
    public double getMovementSpeedModifier() {
        return 0.6; // Slower movement in rain
    }
    
    @Override
    public double getHealthChangeRate() {
        return 1.3; // Faster health decay due to rain stress
    }
}