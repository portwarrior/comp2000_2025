package patterns;

import actors.Actor;

/**
 * Simple strategy for how animals respond to weather
 * Student-level implementation
 */
public interface WeatherStrategy {
    // What should this animal do in this weather?
    void respondToWeather(Actor animal, boolean isRaining, double temperature);
    
    // How urgent is it for this animal to respond?
    int getUrgencyLevel(boolean isRaining, double temperature);
    
    // Get descriptive response for display/notifications
    String getWeatherResponse(boolean isRaining, double temperature);
    
    // What type of animal does this strategy work for?
    String getAnimalType();
}