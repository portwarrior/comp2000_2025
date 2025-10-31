package patterns.decorators;

import java.awt.Color;
import java.awt.Graphics;

/**
 * PHASE 3: Wind Effect Decorator
 * Adds wind-based visual effects to objects
 */
public class WindEffectDecorator extends WeatherEffectDecorator {
    private double windSpeed;
    private double windX;
    private double windY;
    
    public WindEffectDecorator(double windSpeed, double windX, double windY) {
        this.windSpeed = windSpeed;
        this.windX = windX;
        this.windY = windY;
    }
    
    @Override
    public void applyEffect(Graphics g, int x, int y, int width, int height) {
        if (windSpeed <= 0.3) return;
        
        Color originalColor = g.getColor();
        
        // Wind particle streaks
        g.setColor(new Color(200, 200, 200, (int)(windSpeed * 100)));
        
        int particleCount = (int)(windSpeed * 6);
        
        for (int i = 0; i < particleCount; i++) {
            int particleX = x + (int)(Math.random() * width);
            int particleY = y + (int)(Math.random() * height);
            
            // Draw wind streak based on wind direction
            int endX = particleX + (int)(windX * 10);
            int endY = particleY + (int)(windY * 10);
            
            g.drawLine(particleX, particleY, endX, endY);
        }
        
        // Wind distortion effect - slight offset
        if (windSpeed > 0.6) {
            g.setColor(new Color(150, 150, 150, 30));
            int offsetX = (int)(windX * 2);
            int offsetY = (int)(windY * 2);
            g.fillRect(x + offsetX, y + offsetY, width, height);
        }
        
        g.setColor(originalColor);
    }
    
    @Override
    public String getEffectDescription() {
        if (windSpeed > 0.8) return "Buffeted by strong winds";
        if (windSpeed > 0.5) return "Swaying in wind";
        return "Gentle wind movement";
    }
    
    @Override
    public double getEffectIntensity() {
        return Math.max(0.0, (windSpeed - 0.3) / 0.7); // Scale from 0.3-1.0 to 0.0-1.0
    }
    
    @Override
    public boolean isEffectActive(boolean isRaining, double temperature, double windSpeed) {
        return windSpeed > 0.3;
    }
}