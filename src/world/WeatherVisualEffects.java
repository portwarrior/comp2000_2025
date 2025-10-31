package world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles all visual weather effects in the game
 * Creates immersive weather visualization constrained to the grid area
 */
public class WeatherVisualEffects {
    private Random random;
    private List<RainDrop> rainDrops;
    private List<WindParticle> windParticles;
    private long lastEffectUpdate;
    
    // Grid boundaries - rain only appears in the game grid
    private static final int GRID_X = 10;
    private static final int GRID_Y = 10;
    private static final int GRID_WIDTH = 20 * 35; // 20 cells * 35px each = 700px
    private static final int GRID_HEIGHT = 20 * 35; // 20 cells * 35px each = 700px
    
    // Colors for different weather conditions
    private static final Color RAIN_COLOR = new Color(100, 150, 255, 150);
    private static final Color RAIN_SPLASH = new Color(150, 200, 255, 100);
    private static final Color COLD_TINT = new Color(150, 200, 255, 30);
    private static final Color HOT_TINT = new Color(255, 200, 100, 30);
    private static final Color WIND_COLOR = new Color(200, 200, 200, 60);
    
    public WeatherVisualEffects() {
        this.random = new Random();
        this.rainDrops = new ArrayList<>();
        this.windParticles = new ArrayList<>();
        this.lastEffectUpdate = System.currentTimeMillis();
    }
    
    /**
     * Update all weather visual effects
     */
    public void updateEffects() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEffectUpdate > 50) { // Update every 50ms for smooth animation
            updateRainDrops();
            updateWindParticles();
            lastEffectUpdate = currentTime;
        }
    }
    
    /**
     * Render all weather effects on the screen with wind components
     */
    public void render(Graphics2D g2d, int width, int height, boolean isRaining, double temperature, double windX, double windY, double windSpeed) {
        // Apply temperature tint first (background effect)
        renderTemperatureEffects(g2d, width, height, temperature);
        
        // Then render active weather effects
        if (isRaining) {
            renderRain(g2d, windX, windY);
        }
        
        if (windSpeed > 0.3) {
            renderWind(g2d, width, height, windSpeed);
        }
        
        // Update effects for next frame
        updateEffects();
    }
    
    /**
     * Legacy render method for backwards compatibility
     */
    public void render(Graphics2D g2d, int width, int height, boolean isRaining, double temperature, double windSpeed) {
        render(g2d, width, height, isRaining, temperature, 0.0, 0.0, windSpeed);
    }
    
    /**
     * Add rain effect constrained to grid area with realistic animation
     */
    public void addRainEffect(int screenWidth, int screenHeight, double intensity) {
        int rainCount = (int)(intensity * 15); // Reduced count for performance
        
        for (int i = 0; i < rainCount; i++) {
            if (rainDrops.size() < 150) { // Limit rain drops for performance
                // Rain only falls within the grid area
                double startX = GRID_X + random.nextDouble() * GRID_WIDTH;
                double startY = GRID_Y - random.nextInt(30); // Start slightly above grid
                
                RainDrop drop = new RainDrop(
                    startX,
                    startY,
                    3 + random.nextDouble() * 2, // Random fall speed
                    8 + random.nextInt(15), // Random length (shorter, more realistic)
                    0.2 + random.nextDouble() * 0.3 // Random opacity
                );
                rainDrops.add(drop);
            }
        }
    }
    
    /**
     * Add wind effect based on actual wind direction data from server
     */
    public void addWindEffect(int screenWidth, int screenHeight, double windX, double windY, double strength) {
        int particleCount = (int)(strength * 30); // Reduced for better performance
        
        for (int i = 0; i < particleCount; i++) {
            if (windParticles.size() < 80) { // Limit particles for performance
                // Create wind particles that move with actual wind direction
                WindParticle particle = new WindParticle(
                    GRID_X + random.nextDouble() * GRID_WIDTH, // Only in grid area
                    GRID_Y + random.nextDouble() * GRID_HEIGHT,
                    windX * strength * 50, // Scale wind speed for visibility
                    windY * strength * 50,
                    30 + random.nextInt(60) // Shorter lifespan for better performance
                );
                windParticles.add(particle);
            }
        }
    }
    

    
    private void renderRain(Graphics2D g2d, double windX, double windY) {
        for (RainDrop drop : rainDrops) {
            // Create rain color with individual drop opacity
            Color rainColor = new Color(100, 150, 255, (int)(drop.opacity * 180));
            g2d.setColor(rainColor);
            g2d.setStroke(new BasicStroke(1.5f));
            
            // Calculate wind effect on rain angle
            int windOffsetX = (int)(windX * drop.length * 0.5); // Wind affects rain angle
            int windOffsetY = (int)(windY * drop.length * 0.3);
            
            // Draw rain drop as wind-affected line
            int startX = (int)drop.x;
            int startY = (int)drop.y;
            int endX = startX + windOffsetX + 1; // Base angle + wind effect
            int endY = startY + (int)drop.length + windOffsetY;
            
            g2d.drawLine(startX, startY, endX, endY);
            
            // Add enhanced splash effect when drop hits bottom of grid
            if (drop.y + drop.length >= GRID_Y + GRID_HEIGHT - 8) {
                g2d.setColor(new Color(150, 200, 255, (int)(drop.opacity * 100)));
                // Wind affects splash direction
                int splashX = (int)drop.x + (int)(windX * 3);
                g2d.fillOval(splashX - 3, (int)(drop.y + drop.length) - 2, 6, 3);
                
                // Add small droplets for splash effect
                for (int i = 0; i < 2; i++) {
                    int dropletX = splashX + (int)(random.nextGaussian() * 4);
                    int dropletY = (int)(drop.y + drop.length) - random.nextInt(3);
                    g2d.fillOval(dropletX, dropletY, 1, 1);
                }
            }
        }
    }
    
    private void renderWind(Graphics2D g2d, int width, int height, double windSpeed) {
        g2d.setColor(WIND_COLOR);
        g2d.setStroke(new BasicStroke(1.0f));
        
        for (WindParticle particle : windParticles) {
            // Draw wind as small moving lines
            int alpha = (int)(particle.life * 255 / particle.maxLife);
            g2d.setColor(new Color(200, 200, 200, Math.max(0, Math.min(255, alpha))));
            
            g2d.drawLine(
                (int)particle.x, (int)particle.y,
                (int)(particle.x + particle.vx * 3), (int)(particle.y + particle.vy * 3)
            );
        }
    }
    
    private void renderTemperatureEffects(Graphics2D g2d, int width, int height, double temperature) {
        // Create temperature overlay
        if (temperature < 0.3) {
            // Cold effect - blue tint
            g2d.setColor(COLD_TINT);
            g2d.fillRect(0, 0, width, height);
        } else if (temperature > 0.7) {
            // Hot effect - warm tint
            g2d.setColor(HOT_TINT);
            g2d.fillRect(0, 0, width, height);
        }
    }
    
    private void updateRainDrops() {
        rainDrops.removeIf(drop -> {
            drop.y += drop.speed;
            // Remove drops that fall below the grid area
            return drop.y > GRID_Y + GRID_HEIGHT + 20;
        });
    }
    
    private void updateWindParticles() {
        windParticles.removeIf(particle -> {
            particle.x += particle.vx;
            particle.y += particle.vy;
            particle.life--;
            return particle.life <= 0 || particle.x < -50 || particle.x > 1074 || particle.y < -50 || particle.y > 770; // Use actual screen bounds
        });
    }
    
    /**
     * Simple rain drop representation with opacity
     */
    private static class RainDrop {
        double x, y, speed;
        int length;
        double opacity;
        
        RainDrop(double x, double y, double speed, int length, double opacity) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.length = length;
            this.opacity = opacity;
        }
    }
    
    /**
     * Wind particle representation
     */
    private static class WindParticle {
        double x, y, vx, vy;
        int life, maxLife;
        
        WindParticle(double x, double y, double vx, double vy, int maxLife) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.life = maxLife;
            this.maxLife = maxLife;
        }
    }
}