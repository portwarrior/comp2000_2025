package patterns;

import actors.Actor;

/**
 * How cats respond to weather - simple student implementation
 */
public class CatWeatherStrategy implements WeatherStrategy {
    
    @Override
    public void respondToWeather(Actor animal, boolean isRaining, double temperature) {
        // Cats hate rain and cold
        if (isRaining) {
            // Rain makes cats unhappy
            animal.takeDamage(5);
        }
        
        if (temperature < 0.3) {
            // Cats need warmth when cold
            animal.takeDamage(3);
        }
        
        if (temperature > 0.7) {
            // Cats like warm but not too hot
        }
    }
    
    @Override
    public int getUrgencyLevel(boolean isRaining, double temperature) {
        int urgency = 0;
        
        if (isRaining) urgency += 8; // Cats really hate rain
        if (temperature < 0.3) urgency += 6; // Cold is bad for cats
        if (temperature > 0.8) urgency += 4; // Too hot is uncomfortable
        
        return urgency;
    }
    
    @Override
    public String getWeatherResponse(boolean isRaining, double temperature) {
        if (isRaining && temperature < 0.3) {
            return "hiding from cold rain";
        } else if (isRaining) {
            return "hiding from rain";
        } else if (temperature < 0.3) {
            return "seeking warmth";
        } else if (temperature > 0.7) {
            return "finding shade";
        } else {
            return "lounging comfortably";
        }
    }
    
    @Override
    public String getAnimalType() {
        return "Cat";
    }
}