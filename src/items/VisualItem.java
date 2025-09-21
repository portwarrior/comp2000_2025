package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import world.Cell;

public class VisualItem {
    
    private Item item;
    private Cell location;
    private Color color;
    private Image icon;
    
    public VisualItem(Item item, Cell location) {
        this.item = item;
        this.location = location;
        this.color = getColorForItem(item);
        this.icon = getIconForItem(item);
    }
    
    public void paint(Graphics g) {
        if (location == null) return;
        
        int x = location.x;
        int y = location.y;
        int size = Cell.SIZE;
        
        g.setColor(color);
        g.fillOval(x + size/4, y + size/4, size/2, size/2);
        
        if (icon != null) {
            int imgSize = size / 2;
            g.drawImage(icon, x + size/4, y + size/4, imgSize, imgSize, null);
        } else {
            g.setColor(Color.BLACK);
            g.setFont(g.getFont().deriveFont(12f));
            
            String symbol = getTextSymbolForItem(item);
            int textX = x + size/2 - 4;
            int textY = y + size/2 + 4;
            g.drawString(symbol, textX, textY);
        }
        
        g.setColor(Color.DARK_GRAY);
        g.drawOval(x + size/4, y + size/4, size/2, size/2);
    }
    
    private String getTextSymbolForItem(Item item) {
        String itemName = item.getName().toLowerCase();
        
        if (itemName.contains("bone")) return "B";
        else if (itemName.contains("milk")) return "M";
        else if (itemName.contains("water")) return "W";
        else if (itemName.contains("worm")) return "~";
        else if (itemName.contains("bowl")) return "♦";
        else if (itemName.contains("cup")) return "C";
        else if (itemName.contains("saucer")) return "S";
        else if (itemName.contains("tool")) return "T";
        else return "?";
    }
    
    private Color getColorForItem(Item item) {
        String itemName = item.getName().toLowerCase();
        
        if (itemName.contains("bone")) {
            return new Color(255, 248, 220);
        } else if (itemName.contains("milk")) {
            return new Color(255, 255, 255);
        } else if (itemName.contains("water")) {
            return new Color(173, 216, 230);
        } else if (itemName.contains("worm")) {
            return new Color(139, 69, 19);
        } else if (itemName.contains("bowl")) {
            return new Color(192, 192, 192);
        } else if (itemName.contains("cup")) {
            return new Color(220, 220, 220);
        } else if (itemName.contains("saucer")) {
            return new Color(180, 180, 180);
        } else if (itemName.contains("tool")) {
            return new Color(160, 160, 160);
        } else {
            return new Color(255, 255, 0);
        }
    }
    
    private Image getIconForItem(Item item) {
        String itemName = item.getName().toLowerCase();
        String imageName = getImageName(itemName);
        
        if (imageName != null) {
            return loadImage(imageName);
        }
        
        return null;
    }
    
    private String getImageName(String itemName) {
        if (itemName.contains("bone")) return "bone.png";
        else if (itemName.contains("milk")) return "milk.png";
        else if (itemName.contains("water")) return "drop.png";
        else if (itemName.contains("worm")) return "worm.png";
        else if (itemName.contains("bowl")) return "bowl.png";
        else if (itemName.contains("cup")) return "cup.png";
        else if (itemName.contains("saucer")) return "saucer.png";
        else if (itemName.contains("tool")) return "tool.png";
        return null;
    }
    
    private Image loadImage(String imageName) {
        String[] paths = {
            "/actors/scribes/" + imageName,
            "src/actors/scribes/" + imageName,
            "actors/scribes/" + imageName,
            "/scribes/" + imageName,
            "src/scribes/" + imageName,
            "scribes/" + imageName
        };
        
        for (String path : paths) {
            try {
                if (path.startsWith("/")) {
                    java.net.URL imageUrl = getClass().getResource(path);
                    if (imageUrl != null) {
                        return new ImageIcon(imageUrl).getImage();
                    }
                } else {
                    java.io.File imageFile = new java.io.File(path);
                    if (imageFile.exists()) {
                        return new ImageIcon(imageFile.getAbsolutePath()).getImage();
                    }
                }
            } catch (Exception e) {
                // Continue to next path
            }
        }
        
        return null;
    }
    
    public Item getItem() { 
        return item; 
    }
    
    public Cell getLocation() { 
        return location; 
    }
    
    public void setLocation(Cell newLocation) {
        this.location = newLocation;
    }
    
    public boolean isAt(Cell cell) {
        return location != null && cell != null && 
               location.col == cell.col && location.row == cell.row;
    }
}
