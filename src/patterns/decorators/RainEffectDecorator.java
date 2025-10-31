package patterns.decorators;

import java.awt.Color;
import java.awt.Graphics;

/**
 * PHASE 3: Rain Effect Decorator
 * Adds rain droplets and wet appearance to objects
 */
public class RainEffectDecorator extends WeatherEffectDecorator {
    private double rainIntensity;
    
    public RainEffectDecorator(double rainIntensity) {
        this.rainIntensity = rainIntensity;
    }
    
    @Override
    public void applyEffect(Graphics g, int x, int y, int width, int height) {
        if (rainIntensity <= 0) return;
        
        // Create rain droplets on the object
        Color originalColor = g.getColor();
        
        // Rain droplets
        g.setColor(new Color(150, 200, 255, (int)(rainIntensity * 150)));
        
        int dropletCount = (int)(rainIntensity * 8); // More droplets with higher intensity
        
        for (int i = 0; i < dropletCount; i++) {
            int dropX = x + (int)(Math.random() * width);
            int dropY = y + (int)(Math.random() * height);
            
            // Draw small droplets
            g.fillOval(dropX, dropY, 2, 3);
        }
        
        // Wet surface effect - darker tint
        g.setColor(new Color(0, 0, 50, (int)(rainIntensity * 40)));
        g.fillRect(x, y, width, height);
        
        g.setColor(originalColor);
    }
    
    @Override
    public String getEffectDescription() {
        if (rainIntensity > 0.7) return "Soaked in heavy rain";
        if (rainIntensity > 0.4) return "Wet from rain";
        return "Light rain droplets";
    }
    
    @Override
    public double getEffectIntensity() {
        return rainIntensity;
    }
    
    @Override
    public boolean isEffectActive(boolean isRaining, double temperature, double windSpeed) {
        return isRaining && rainIntensity > 0.1;
    }
}