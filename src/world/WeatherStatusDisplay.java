package world;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import actors.Actor;
import weather.SimpleWeatherManager;
import patterns.WeatherStrategyManager;
// Debug class is in the root package, so we can reference it directly

/**
 * Comprehensive weather status display system
 * Shows weather conditions, animal health, and weather notifications
 */
public class WeatherStatusDisplay {
    private SimpleWeatherManager weatherManager;
    private WeatherStrategyManager strategyManager;
    private Font headerFont;
    private Font normalFont;
    private Font smallFont;
    
    // Colors for different moods and conditions
    private static final Color HEALTHY_COLOR = new Color(80, 200, 80);
    private static final Color WARNING_COLOR = new Color(255, 180, 50);
    private static final Color DANGER_COLOR = new Color(220, 80, 80);
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 180);
    private static final Color TEXT_COLOR = Color.WHITE;
    
    public WeatherStatusDisplay(SimpleWeatherManager weatherManager, WeatherStrategyManager strategyManager) {
        this.weatherManager = weatherManager;
        this.strategyManager = strategyManager;
        this.headerFont = new Font("Arial", Font.BOLD, 14);
        this.normalFont = new Font("Arial", Font.PLAIN, 12);
        this.smallFont = new Font("Arial", Font.PLAIN, 10);
    }
    
    /**
     * Render the complete weather status panel (only in debug mode)
     */
    public void renderWeatherStatus(Graphics2D g2d, int screenWidth, int screenHeight) {
        // Only render weather panels in debug mode
        if (!"true".equals(System.getProperty("DEBUG_MODE"))) {
            return;
        }
        
        // Weather info panel (positioned to not overlap with existing UI)
        renderWeatherPanel(g2d, screenWidth - 250, 60);
        
        // Weather legend (positioned below the main panel)
        renderWeatherLegend(g2d, screenWidth - 230, screenHeight - 140);
    }
    
    /**
     * Render animal status indicators next to each animal
     */
    public void renderAnimalStatus(Graphics2D g2d, Actor actor, int x, int y) {
        // Get current weather conditions for this animal's location
        Cell location = actor.getLocation();
        if (location == null) return;
        
        int gridX = location.col - 'A';
        int gridY = location.row;
        
        boolean isRaining = weatherManager.isRainingAt(gridX, gridY);
        double temperature = weatherManager.getTemperatureAt(gridX, gridY);
        
        // Calculate weather stress level
        int urgency = strategyManager.getUrgencyLevel(actor, isRaining, temperature);
        
        // Draw health bar
        renderHealthBar(g2d, x, y - 35, actor.getCurrentHealth(), actor.getMaxHealth());
        
        // Draw weather mood indicator
        renderWeatherMoodIndicator(g2d, x, y - 50, urgency, isRaining, temperature);
        
        // Draw weather effects text
        renderWeatherEffectsText(g2d, x, y - 20, actor, isRaining, temperature);
    }
    
    private void renderWeatherPanel(Graphics2D g2d, int x, int y) {
        // Background panel
        RoundRectangle2D panel = new RoundRectangle2D.Double(x, y, 210, 100, 10, 10);
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fill(panel);
        g2d.setColor(TEXT_COLOR);
        g2d.draw(panel);
        
        // Header
        g2d.setFont(headerFont);
        g2d.drawString("WEATHER STATUS", x + 10, y + 20);
        
        // Weather summary
        g2d.setFont(normalFont);
        String weatherSummary = weatherManager.getWeatherSummary();
        g2d.drawString(weatherSummary, x + 10, y + 40);
        
        // Current conditions
        g2d.setFont(smallFont);
        if (weatherManager.isWorking()) {
            g2d.setColor(HEALTHY_COLOR);
            g2d.drawString("Weather system active", x + 10, y + 55);
            
            // Show some sample weather data
            g2d.setColor(TEXT_COLOR);
            boolean rainAtCenter = weatherManager.isRainingAt(10, 8);
            double tempAtCenter = weatherManager.getTemperatureAt(10, 8);
            
            g2d.drawString("Center: " + (rainAtCenter ? "Raining" : "Clear"), x + 10, y + 70);
            g2d.drawString("Temp: " + String.format("%.1f", tempAtCenter * 100) + "%", x + 10, y + 85);
        } else {
            g2d.setColor(DANGER_COLOR);
            g2d.drawString("We0ather system offline", x + 10, y + 55);
        }
    }
    
    private void renderWeatherLegend(Graphics2D g2d, int x, int y) {
        // Background panel
        RoundRectangle2D panel = new RoundRectangle2D.Double(x, y, 190, 110, 10, 10);
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fill(panel);
        g2d.setColor(TEXT_COLOR);
        g2d.draw(panel);
        
        // Header
        g2d.setFont(headerFont);
        g2d.drawString("WEATHER EFFECTS", x + 10, y + 20);
        
        // Legend items
        g2d.setFont(smallFont);
        g2d.setColor(new Color(100, 150, 255));
        g2d.fillRect(x + 10, y + 30, 10, 3);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Rain (slows movement)", x + 25, y + 35);
        
        g2d.setColor(new Color(150, 200, 255));
        g2d.fillRect(x + 10, y + 45, 180, 3);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Cold (increases hunger)", x + 25, y + 50);
        
        g2d.setColor(new Color(255, 200, 100));
        g2d.fillRect(x + 10, y + 60, 180, 3);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Heat (increases thirst)", x + 25, y + 65);
        
        g2d.setColor(HEALTHY_COLOR);
        g2d.fillOval(x + 10, y + 75, 8, 8);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Happy animal", x + 25, y + 83);
        
        g2d.setColor(WARNING_COLOR);
        g2d.fillOval(x + 10, y + 90, 8, 8);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Stressed animal", x + 25, y + 98);
    }
    
    private void renderHealthBar(Graphics2D g2d, int x, int y, int currentHealth, int maxHealth) {
        int barWidth = 40;
        int barHeight = 6;
        
        // Background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x - barWidth/2, y, barWidth, barHeight);
        
        // Health bar
        double healthPercent = (double)currentHealth / maxHealth;
        Color healthColor;
        if (healthPercent > 0.7) {
            healthColor = HEALTHY_COLOR;
        } else if (healthPercent > 0.3) {
            healthColor = WARNING_COLOR;
        } else {
            healthColor = DANGER_COLOR;
        }
        
        g2d.setColor(healthColor);
        g2d.fillRect(x - barWidth/2, y, (int)(barWidth * healthPercent), barHeight);
        
        // Border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x - barWidth/2, y, barWidth, barHeight);
    }
    
    private void renderWeatherMoodIndicator(Graphics2D g2d, int x, int y, int urgency, boolean isRaining, double temperature) {
        Color moodColor;
        String moodText;
        
        if (urgency >= 8) {
            moodColor = DANGER_COLOR;
            moodText = "!";
        } else if (urgency >= 5) {
            moodColor = WARNING_COLOR;
            moodText = "?";
        } else {
            moodColor = HEALTHY_COLOR;
            moodText = "♡";
        }
        
        // Draw mood indicator
        g2d.setColor(moodColor);
        g2d.fillOval(x - 6, y, 12, 12);
        g2d.setColor(Color.WHITE);
        g2d.setFont(smallFont);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x - fm.stringWidth(moodText) / 2;
        int textY = y + fm.getAscent() - 1;
        g2d.drawString(moodText, textX, textY);
    }
    
    private void renderWeatherEffectsText(Graphics2D g2d, int x, int y, Actor actor, boolean isRaining, double temperature) {
        g2d.setFont(smallFont);
        g2d.setColor(Color.WHITE);
        
        String effectText = "";
        
        if (isRaining && temperature < 0.3) {
            effectText = "Cold & Wet!";
            g2d.setColor(WARNING_COLOR);
        } else if (isRaining) {
            effectText = "Getting soaked";
            g2d.setColor(new Color(100, 150, 255));
        } else if (temperature > 0.8) {
            effectText = "Too hot!";
            g2d.setColor(WARNING_COLOR);
        } else if (temperature < 0.2) {
            effectText = "Freezing!";
            g2d.setColor(new Color(150, 200, 255));
        } else {
            effectText = "Comfortable";
            g2d.setColor(HEALTHY_COLOR);
        }
        
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x - fm.stringWidth(effectText) / 2;
        g2d.drawString(effectText, textX, y);
    }
}