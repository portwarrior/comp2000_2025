package patterns.states;

import actors.Actor;

/**
 * PHASE 3: Windy Weather State - Animals adjust to wind conditions
 */
public class WindyWeatherState implements WeatherBehaviorState {
    
    @Override
    public void executeBehavior(Actor actor) {
        // Wind behavior - harder to move, affects scent detection, can be beneficial
        
        // Wind makes movement more difficult
        if (Math.random() < 0.4) {
            actor.takeDamage(1); // Energy cost of fighting wind
        }
        
        // But wind can also bring new scents and opportunities
        if (Math.random() < 0.3) {
            actor.heal(2); // Found food carried by wind or good hunting
        }
        
        // Wind affects animal senses - sometimes good, sometimes bad
        if (Math.random() < 0.2) {
            // Wind brings interesting scents - animals are more active
            actor.heal(1);
        }
    }
    
    @Override
    public boolean shouldRemainInState(boolean isRaining, double temperature, double windSpeed) {
        return !isRaining && windSpeed >= 0.4; // Windy but not raining
    }
    
    @Override
    public WeatherBehaviorState getNextState(boolean isRaining, double temperature, double windSpeed) {
        if (isRaining) {
            return new ShelterSeekingState();
        }
        if (windSpeed < 0.4) {
            // Wind died down - check temperature
            if (temperature < 0.3) {
                return new ColdWeatherState();
            }
            if (temperature > 0.7) {
                return new HotWeatherState();
            }
            return new NormalWeatherState();
        }
        return this; // Stay windy
    }
    
    @Override
    public String getStateDescription() {
        return "Adapting to wind - Alert and cautious";
    }
    
    @Override
    public double getMovementSpeedModifier() {
        return 0.8; // Slightly slower due to wind resistance
    }
    
    @Override
    public double getHealthChangeRate() {
        return 1.1; // Slightly faster health decay due to wind stress
    }
}