package world;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import actors.Actor;

/**
 * Weather notification and alert system
 * Shows real-time notifications about weather effects on animals
 */
public class WeatherNotificationSystem {
    private List<WeatherNotification> notifications;
    private Font notificationFont;
    private long lastNotificationTime;
    
    // Constants for notification styling
    private static final Color NOTIFICATION_BG = new Color(0, 0, 0, 200);
    private static final Color WARNING_COLOR = new Color(255, 180, 50);
    private static final Color DANGER_COLOR = new Color(220, 80, 80);
    private static final Color INFO_COLOR = new Color(100, 200, 255);
    private static final Color SUCCESS_COLOR = new Color(80, 200, 80);
    
    public WeatherNotificationSystem() {
        this.notifications = new ArrayList<>();
        this.notificationFont = new Font("Arial", Font.BOLD, 12);
        this.lastNotificationTime = 0;
    }
    
    /**
     * Add a weather-related notification
     */
    public void addNotification(String message, NotificationType type, int duration) {
        // Avoid spam - limit notifications to one per second
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastNotificationTime < 1000) {
            return;
        }
        
        WeatherNotification notification = new WeatherNotification(message, type, duration);
        notifications.add(notification);
        lastNotificationTime = currentTime;
        
        // Limit to 5 notifications on screen
        if (notifications.size() > 5) {
            notifications.remove(0);
        }
    }
    
    /**
     * Check animal health and generate appropriate notifications
     */
    public void checkAnimalWeatherHealth(Actor actor, boolean isRaining, double temperature, int urgency) {
        String animalType = actor.getClass().getSimpleName();
        
        // Check for dangerous health levels
        if (actor.getCurrentHealth() < 30) {
            addNotification(animalType + " is in critical condition! Health: " + actor.getCurrentHealth(), 
                          NotificationType.DANGER, 4000);
        } else if (actor.getCurrentHealth() < 50) {
            addNotification(animalType + " health is low (" + actor.getCurrentHealth() + ") - weather stress!", 
                          NotificationType.WARNING, 3000);
        }
        
        // Check for extreme weather conditions
        if (urgency >= 8) {
            addNotification(animalType + " is in extreme weather distress!", 
                          NotificationType.DANGER, 3000);
        } else if (urgency >= 6) {
            addNotification(animalType + " is struggling with weather conditions", 
                          NotificationType.WARNING, 2500);
        }
        
        // Specific weather condition notifications
        if (isRaining && temperature < 0.2) {
            addNotification(animalType + " is caught in freezing rain!", 
                          NotificationType.DANGER, 3000);
        } else if (temperature > 0.9) {
            addNotification(animalType + " is overheating in extreme heat!", 
                          NotificationType.DANGER, 3000);
        } else if (temperature < 0.1) {
            addNotification(animalType + " is freezing in bitter cold!", 
                          NotificationType.DANGER, 3000);
        }
        
        // Positive notifications for good conditions
        if (urgency <= 2 && actor.getCurrentHealth() > 80) {
            if (Math.random() < 0.01) { // Rare positive messages
                addNotification(animalType + " is enjoying the pleasant weather", 
                              NotificationType.SUCCESS, 2000);
            }
        }
    }
    
    /**
     * Add notification when animals respond to weather
     */
    public void notifyWeatherResponse(Actor actor, String response) {
        String animalType = actor.getClass().getSimpleName();
        addNotification(animalType + ": " + response, NotificationType.INFO, 2000);
    }
    
    /**
     * Add notification for weather system events
     */
    public void notifyWeatherSystemEvent(String event) {
        addNotification("Weather System: " + event, NotificationType.INFO, 3000);
    }
    
    /**
     * Render all active notifications
     */
    public void render(Graphics2D g2d, int screenWidth) {
        updateNotifications();
        
        int y = 60; // Start below weather panel
        Iterator<WeatherNotification> iter = notifications.iterator();
        
        while (iter.hasNext()) {
            WeatherNotification notification = iter.next();
            renderNotification(g2d, notification, screenWidth - 400, y);
            y += 35; // Space between notifications
        }
    }
    
    private void updateNotifications() {
        long currentTime = System.currentTimeMillis();
        notifications.removeIf(notification -> 
            currentTime - notification.creationTime > notification.duration);
    }
    
    private void renderNotification(Graphics2D g2d, WeatherNotification notification, int x, int y) {
        // Calculate fade effect
        long currentTime = System.currentTimeMillis();
        long age = currentTime - notification.creationTime;
        float alpha = 1.0f;
        
        if (age > notification.duration - 1000) {
            // Fade out in the last second
            alpha = (notification.duration - age) / 1000.0f;
            alpha = Math.max(0, Math.min(1, alpha));
        }
        
        // Set up graphics with transparency
        Composite originalComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        // Measure text
        g2d.setFont(notificationFont);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(notification.message);
        int textHeight = fm.getHeight();
        
        // Background
        RoundRectangle2D background = new RoundRectangle2D.Double(
            x, y, textWidth + 20, textHeight + 10, 8, 8);
        g2d.setColor(NOTIFICATION_BG);
        g2d.fill(background);
        
        // Border color based on type
        Color borderColor = getColorForType(notification.type);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(background);
        
        // Text
        g2d.setColor(Color.WHITE);
        g2d.drawString(notification.message, x + 10, y + textHeight);
        
        // Icon based on type
        renderNotificationIcon(g2d, notification.type, x + textWidth + 5, y + 5);
        
        // Restore original composite
        g2d.setComposite(originalComposite);
    }
    
    private void renderNotificationIcon(Graphics2D g2d, NotificationType type, int x, int y) {
        g2d.setColor(getColorForType(type));
        
        switch (type) {
            case DANGER:
                // Exclamation mark
                g2d.fillOval(x, y, 12, 12);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                g2d.drawString("!", x + 4, y + 9);
                break;
            case WARNING:
                // Triangle
                int[] triangleX = {x + 6, x + 1, x + 11};
                int[] triangleY = {y + 1, y + 11, y + 11};
                g2d.fillPolygon(triangleX, triangleY, 3);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 8));
                g2d.drawString("!", x + 5, y + 9);
                break;
            case INFO:
                // Circle with 'i'
                g2d.fillOval(x, y, 12, 12);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                g2d.drawString("i", x + 4, y + 9);
                break;
            case SUCCESS:
                // Checkmark circle
                g2d.fillOval(x, y, 12, 12);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x + 3, y + 6, x + 5, y + 8);
                g2d.drawLine(x + 5, y + 8, x + 9, y + 4);
                break;
        }
    }
    
    private Color getColorForType(NotificationType type) {
        switch (type) {
            case DANGER: return DANGER_COLOR;
            case WARNING: return WARNING_COLOR;
            case INFO: return INFO_COLOR;
            case SUCCESS: return SUCCESS_COLOR;
            default: return INFO_COLOR;
        }
    }
    
    /**
     * Notification types for different severity levels
     */
    public enum NotificationType {
        INFO, WARNING, DANGER, SUCCESS
    }
    
    /**
     * Individual notification representation
     */
    private static class WeatherNotification {
        String message;
        NotificationType type;
        long creationTime;
        int duration;
        
        WeatherNotification(String message, NotificationType type, int duration) {
            this.message = message;
            this.type = type;
            this.duration = duration;
            this.creationTime = System.currentTimeMillis();
        }
    }
}