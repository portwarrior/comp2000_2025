package patterns.decorators;

import java.awt.Color;
import java.awt.Graphics;

/**
 * PHASE 3: Temperature Effect Decorator
 * Adds temperature-based visual effects to objects
 */
public class TemperatureEffectDecorator extends WeatherEffectDecorator {
    private double temperature;
    
    public TemperatureEffectDecorator(double temperature) {
        this.temperature = temperature;
    }
    
    @Override
    public void applyEffect(Graphics g, int x, int y, int width, int height) {
        Color originalColor = g.getColor();
        
        if (temperature < 0.3) {
            // Cold effect - blue/white frost tint
            g.setColor(new Color(200, 220, 255, 60));
            g.fillRect(x, y, width, height);
            
            // Add frost crystals
            g.setColor(new Color(255, 255, 255, 120));
            for (int i = 0; i < 4; i++) {
                int frostX = x + (int)(Math.random() * width);
                int frostY = y + (int)(Math.random() * height);
                g.fillOval(frostX, frostY, 1, 1);
            }
            
        } else if (temperature > 0.7) {
            // Hot effect - yellow/red heat shimmer
            g.setColor(new Color(255, 200, 100, 50));
            g.fillRect(x, y, width, height);
            
            // Heat shimmer lines
            g.setColor(new Color(255, 150, 50, 80));
            for (int i = 0; i < 3; i++) {
                int shimmerY = y + (int)(Math.random() * height);
                g.drawLine(x, shimmerY, x + width, shimmerY);
            }
        }
        
        g.setColor(originalColor);
    }
    
    @Override
    public String getEffectDescription() {
        if (temperature < 0.2) return "Frozen solid";
        if (temperature < 0.3) return "Frosted over";
        if (temperature > 0.8) return "Heat shimmering";
        if (temperature > 0.7) return "Heated";
        return "Normal temperature";
    }
    
    @Override
    public double getEffectIntensity() {
        if (temperature < 0.3) {
            return (0.3 - temperature) / 0.3; // 0 to 1 for cold
        } else if (temperature > 0.7) {
            return (temperature - 0.7) / 0.3; // 0 to 1 for hot
        }
        return 0.0; // No effect for normal temperatures
    }
    
    @Override
    public boolean isEffectActive(boolean isRaining, double temperature, double windSpeed) {
        return temperature < 0.3 || temperature > 0.7;
    }
}