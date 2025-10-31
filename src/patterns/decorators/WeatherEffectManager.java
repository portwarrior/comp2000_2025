package patterns.decorators;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import weather.WeatherConditions;

/**
 * PHASE 3: Weather Effect Manager
 * Manages and applies multiple weather effect decorators
 */
public class WeatherEffectManager {
    private List<WeatherEffectDecorator> activeEffects;
    private Random random;
    
    public WeatherEffectManager() {
        this.activeEffects = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Update active effects based on current weather conditions
     */
    public void updateEffects(WeatherConditions weather) {
        activeEffects.clear();
        
        // Add rain effects if raining
        if (weather.isRaining()) {
            activeEffects.add(new RainEffectDecorator(weather.rainIntensity));
        }
        
        // Add temperature effects for extreme temperatures
        if (weather.isCold() || weather.isHot()) {
            activeEffects.add(new TemperatureEffectDecorator(weather.temperature));
        }
        
        // Add wind effects if windy
        if (weather.isWindy()) {
            activeEffects.add(new WindEffectDecorator(weather.getWindSpeed(), weather.windX, weather.windY));
        }
    }
    
    /**
     * Apply all active weather effects to an object
     */
    public void applyEffectsToObject(Graphics g, int x, int y, int width, int height, 
                                   boolean isRaining, double temperature, double windSpeed) {
        for (WeatherEffectDecorator effect : activeEffects) {
            if (effect.isEffectActive(isRaining, temperature, windSpeed)) {
                effect.applyEffect(g, x, y, width, height);
            }
        }
    }
    
    /**
     * Get descriptions of all active effects
     */
    public List<String> getActiveEffectDescriptions(boolean isRaining, double temperature, double windSpeed) {
        List<String> descriptions = new ArrayList<>();
        
        for (WeatherEffectDecorator effect : activeEffects) {
            if (effect.isEffectActive(isRaining, temperature, windSpeed)) {
                descriptions.add(effect.getEffectDescription());
            }
        }
        
        return descriptions;
    }
    
    /**
     * Get total effect intensity (0.0 to 1.0)
     */
    public double getTotalEffectIntensity() {
        double totalIntensity = 0.0;
        int activeCount = 0;
        
        for (WeatherEffectDecorator effect : activeEffects) {
            totalIntensity += effect.getEffectIntensity();
            activeCount++;
        }
        
        return activeCount > 0 ? totalIntensity / activeCount : 0.0;
    }
    
    /**
     * Check if any weather effects are currently active
     */
    public boolean hasActiveEffects(boolean isRaining, double temperature, double windSpeed) {
        return activeEffects.stream()
                .anyMatch(effect -> effect.isEffectActive(isRaining, temperature, windSpeed));
    }
}