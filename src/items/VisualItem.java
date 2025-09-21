package items;

import java.awt.Color;
import java.awt.Graphics;
import world.Cell;

/**
 * Visual representation of an item on the game grid
 */
public class VisualItem {
    
    private Item item;
    private Cell location;
    private Color color;
    private String symbol;
    
    public VisualItem(Item item, Cell location) {
        this.item = item;
        this.location = location;
        this.color = getColorForItem(item);
        this.symbol = getSymbolForItem(item);
    }
    
    /**
     * Draw the item icon on the grid
     */
    public void paint(Graphics g) {
        if (location == null) return;
        
        int x = location.x;
        int y = location.y;
        int size = Cell.SIZE;
        
        // Draw item background
        g.setColor(color);
        g.fillOval(x + size/4, y + size/4, size/2, size/2);
        
        // Draw item symbol
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(12f));
        
        // Center the text
        int textX = x + size/2 - 4;
        int textY = y + size/2 + 4;
        g.drawString(symbol, textX, textY);
        
        // Draw border
        g.setColor(Color.DARK_GRAY);
        g.drawOval(x + size/4, y + size/4, size/2, size/2);
    }
    
    /**
     * Get the color for different item types
     */
    private Color getColorForItem(Item item) {
        String itemName = item.getName().toLowerCase();
        
        if (itemName.contains("bone")) {
            return new Color(255, 248, 220); // Bone white
        } else if (itemName.contains("milk")) {
            return new Color(255, 255, 255); // Pure white
        } else if (itemName.contains("water")) {
            return new Color(173, 216, 230); // Light blue
        } else if (itemName.contains("worm")) {
            return new Color(139, 69, 19); // Brown
        } else if (itemName.contains("bowl") || itemName.contains("tool")) {
            return new Color(192, 192, 192); // Silver
        } else {
            return new Color(255, 255, 0); // Yellow default
        }
    }
    
    /**
     * Get the symbol for different item types
     */
    private String getSymbolForItem(Item item) {
        String itemName = item.getName().toLowerCase();
        
        if (itemName.contains("bone")) {
            return "🦴"; // Bone emoji or fallback
        } else if (itemName.contains("milk")) {
            return "🥛"; // Milk glass emoji
        } else if (itemName.contains("water")) {
            return "💧"; // Water drop emoji
        } else if (itemName.contains("worm")) {
            return "🪱"; // Worm emoji
        } else if (itemName.contains("bowl")) {
            return "🥣"; // Bowl emoji
        } else if (itemName.contains("tool")) {
            return "🔧"; // Tool emoji
        } else {
            return "?"; // Unknown
        }
    }
    
    // Fallback symbols if emojis don't work
    private String getFallbackSymbol(Item item) {
        String itemName = item.getName().toLowerCase();
        
        if (itemName.contains("bone")) {
            return "B";
        } else if (itemName.contains("milk")) {
            return "M";
        } else if (itemName.contains("water")) {
            return "W";
        } else if (itemName.contains("worm")) {
            return "~";
        } else if (itemName.contains("bowl") || itemName.contains("tool")) {
            return "T";
        } else {
            return "?";
        }
    }
    
    // Getters
    public Item getItem() { return item; }
    public Cell getLocation() { return location; }
    
    // Update location when item is moved
    public void setLocation(Cell newLocation) {
        this.location = newLocation;
    }
    
    /**
     * Check if this visual item is at a specific cell
     */
    public boolean isAt(Cell cell) {
        return location != null && cell != null && 
               location.col == cell.col && location.row == cell.row;
    }
}