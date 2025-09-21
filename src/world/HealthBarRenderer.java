package world;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import actors.Actor;

/**
 * health bar renderer
 */
public class HealthBarRenderer {
    
    // dimensions and styling
    private static final int BAR_WIDTH = 200;
    private static final int BAR_HEIGHT = 20;
    private static final int BAR_BORDER = 2;
    private static final int SEGMENT_GAP = 1;
    private static final int SEGMENTS = 10;
    
    // Colors
    private static final Color HEALTH_HIGH = new Color(0, 220, 0);    // Bright green
    private static final Color HEALTH_MID = new Color(255, 165, 0);   // Orange  
    private static final Color HEALTH_LOW = new Color(220, 0, 0);     // Red
    private static final Color HEALTH_CRITICAL = new Color(150, 0, 0); // Dark red
    private static final Color BAR_BACKGROUND = new Color(40, 40, 40); // Dark gray
    private static final Color BAR_BORDER_COLOR = new Color(200, 200, 200); // Light gray
    private static final Color NAME_COLOR = new Color(0, 0, 0); // Black
    
    /**
     * @param g Graphics context
     * @param actor The animal to draw health for
     * @param x X position of the health bar
     * @param y Y position of the health bar
     */
    public static void drawHealthBar(Graphics2D g, Actor actor, int x, int y) {
        // Get health percentage
        double healthPercent = actor.getHealthPercentage();
        
        // animal name above health bar
        g.setColor(NAME_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String animalName = actor.getClass().getSimpleName().toUpperCase();
        g.drawString(animalName, x, y - 5);
        
        // health bar background with border
        g.setColor(BAR_BORDER_COLOR);
        g.fillRect(x - BAR_BORDER, y - BAR_BORDER, 
                  BAR_WIDTH + (BAR_BORDER * 2), BAR_HEIGHT + (BAR_BORDER * 2));
        
        g.setColor(BAR_BACKGROUND);
        g.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);
        
        // segment width
        int segmentWidth = (BAR_WIDTH - (SEGMENTS - 1) * SEGMENT_GAP) / SEGMENTS;
        int filledSegments = (int) Math.ceil(healthPercent * SEGMENTS);
        
        // health segments
        for (int i = 0; i < SEGMENTS; i++) {
            int segmentX = x + i * (segmentWidth + SEGMENT_GAP);
            
            if (i < filledSegments) {
                // Determine segment color
                Color segmentColor = getHealthColor(healthPercent, (double) i / SEGMENTS);
                
                //  gradient effect
                GradientPaint gradient = new GradientPaint(
                    segmentX, y, segmentColor.brighter(),
                    segmentX, y + BAR_HEIGHT, segmentColor.darker()
                );
                g.setPaint(gradient);
                g.fillRect(segmentX, y, segmentWidth, BAR_HEIGHT);
                g.setColor(segmentColor.brighter().brighter());
                g.fillRect(segmentX, y, segmentWidth, 3);
            }
        }
        
        // health text overlay
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        String healthText = actor.getCurrentHealth() + "/" + actor.getMaxHealth();
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (BAR_WIDTH - fm.stringWidth(healthText)) / 2;
        int textY = y + (BAR_HEIGHT + fm.getAscent()) / 2;
        
        g.setColor(Color.BLACK);
        g.drawString(healthText, textX + 1, textY + 1);
        g.setColor(Color.WHITE);
        g.drawString(healthText, textX, textY);
        
        // pulsing effect when critical health
        if (healthPercent < 0.2) {
            drawCriticalHealthEffect(g, x, y);
        }
    }
    
   
    private static Color getHealthColor(double healthPercent, double segmentPosition) {
        if (healthPercent < 0.2) {
            // Critical health - dark red with pulsing
            return HEALTH_CRITICAL;
        } else if (healthPercent < 0.4) {
            // Low health - red
            return HEALTH_LOW;
        } else if (healthPercent < 0.7) {
            // Medium health - orange
            return HEALTH_MID;
        } else {
            // High health - green
            return HEALTH_HIGH;
        }
    }
    
    /**
     * Draw critical health warning effect
     */
    private static void drawCriticalHealthEffect(Graphics2D g, int x, int y) {
        // red glow around the health bar when critical health
        long time = System.currentTimeMillis();
        double pulse = (Math.sin(time / 200.0) + 1) / 2; 
        
        Color glowColor = new Color(255, 0, 0, (int)(pulse * 50 + 30)); // red glow
        g.setColor(glowColor);
        g.fillRect(x - 4, y - 4, BAR_WIDTH + 8, BAR_HEIGHT + 8);
    }
    
    /**
     * Draw multiple health bars for a list of actors
     * @param g Graphics context  
     * @param actors List of actors to draw health bars for
     * @param startX Starting X position
     * @param startY Starting Y position
     * @param spacing Vertical spacing between health bars
     */
    public static void drawHealthBars(Graphics2D g, java.util.List<Actor> actors, 
                                     int startX, int startY, int spacing) {
        for (int i = 0; i < actors.size(); i++) {
            drawHealthBar(g, actors.get(i), startX, startY + i * spacing);
        }
    }
}