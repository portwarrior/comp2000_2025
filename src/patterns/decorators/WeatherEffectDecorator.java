package patterns.decorators;

import java.awt.Graphics;

/**
 * PHASE 3: Weather Effect Decorator Pattern
 * Base decorator for adding weather effects to game objects
 */
public abstract class WeatherEffectDecorator {
    
    /**
     * Apply weather effect to graphics rendering
     */
    public abstract void applyEffect(Graphics g, int x, int y, int width, int height);
    
    /**
     * Get the description of this weather effect
     */
    public abstract String getEffectDescription();
    
    /**
     * Get the intensity of this effect (0.0 to 1.0)
     */
    public abstract double getEffectIntensity();
    
    /**
     * Check if this effect should be active given current conditions
     */
    public abstract boolean isEffectActive(boolean isRaining, double temperature, double windSpeed);
}