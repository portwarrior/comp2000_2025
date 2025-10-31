package patterns;

import actors.Actor;

/**
 * How dogs respond to weather - simple student implementation
 */
public class DogWeatherStrategy implements WeatherStrategy {
    
    @Override
    public void respondToWeather(Actor animal, boolean isRaining, double temperature) {
        // Dogs don't mind rain as much as cats
        if (isRaining) {
            // Dogs actually like rain a bit
            animal.heal(2);
        }
        
        if (temperature < 0.2) {
            // Dogs handle cold better than cats but still don't like it
            animal.takeDamage(2);
        }
        
        if (temperature > 0.8) {
            // Dogs struggle with heat
            animal.takeDamage(4);
        }
    }
    
    @Override
    public int getUrgencyLevel(boolean isRaining, double temperature) {
        int urgency = 0;
        
        if (isRaining) urgency += 2; // Dogs don't mind rain much
        if (temperature < 0.2) urgency += 5; // Cold is moderately bad
        if (temperature > 0.8) urgency += 7; // Heat is worse for dogs
        
        return urgency;
    }
    
    @Override
    public String getWeatherResponse(boolean isRaining, double temperature) {
        if (isRaining && temperature > 0.5) {
            return "joyfully playing in warm rain";
        } else if (isRaining) {
            return "playing in the rain";
        } else if (temperature < 0.2) {
            return "shivering in the cold";
        } else if (temperature > 0.8) {
            return "panting in the heat";
        } else {
            return "enjoying the nice weather";
        }
    }
    
    @Override
    public String getAnimalType() {
        return "Dog";
    }
}