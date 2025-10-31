package patterns;

import actors.Actor;

/**
 * How birds respond to weather - simple student implementation
 */
public class BirdWeatherStrategy implements WeatherStrategy {
    
    @Override
    public void respondToWeather(Actor animal, boolean isRaining, double temperature) {
        // Birds are very sensitive to weather
        if (isRaining) {
            // Rain is bad for birds
            animal.takeDamage(6);
        }
        
        if (temperature < 0.4) {
            // Cold is very bad for small birds
            animal.takeDamage(8);
        }
        
        if (temperature > 0.7) {
            // Heat is moderately bad for birds
            animal.takeDamage(3);
        }
        
        // Birds like mild weather
        if (!isRaining && temperature >= 0.4 && temperature <= 0.7) {
            animal.heal(1);
        }
    }
    
    @Override
    public int getUrgencyLevel(boolean isRaining, double temperature) {
        int urgency = 0;
        
        if (isRaining) urgency += 9; // Rain is very urgent for birds
        if (temperature < 0.4) urgency += 10; // Cold is critical for birds
        if (temperature > 0.7) urgency += 5; // Heat is moderately urgent
        
        return urgency;
    }
    
    @Override
    public String getWeatherResponse(boolean isRaining, double temperature) {
        if (isRaining && temperature < 0.4) {
            return "desperately seeking warm shelter";
        } else if (isRaining) {
            return "seeking shelter from rain";
        } else if (temperature < 0.4) {
            return "fluffing feathers for warmth";
        } else if (temperature > 0.7) {
            return "spreading wings to cool down";
        } else {
            return "enjoying the pleasant weather";
        }
    }
    
    @Override
    public String getAnimalType() {
        return "Bird";
    }
}