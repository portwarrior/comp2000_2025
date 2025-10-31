package world;

import items.Item;
import items.VisualItem;
import items.consumables.*;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages visual items on game grid
 */
public class ItemManager {
    
    private List<VisualItem> visualItems;
    
    public ItemManager() {
        this.visualItems = new ArrayList<>();
    }
    
    /**
     * Spawn an item at specific location
     */
    public void spawnItem(String itemType, Cell location) {
        Item item = createItemByType(itemType);
        if (item != null) {
            VisualItem visualItem = new VisualItem(item, location);
            visualItems.add(visualItem);
            if ("true".equals(System.getProperty("DEBUG_MODE"))) {
                System.out.println("Spawned " + itemType + " at " + location.col + "," + location.row);
            }
        }
    }
    
    /**
     * Create an item instance based on type name
     */
    private Item createItemByType(String itemType) {
        switch (itemType.toLowerCase()) {
            case "bone":
                return new Bone();
            case "milk":
                return new Milk();
            case "water":
                return new Water();
            case "worm":
                return new Worm();
            case "bowl":
                return new SimpleToolItem("Bowl", 1);
            case "cup":
                return new SimpleToolItem("Cup", 1);
            case "saucer":
                return new SimpleToolItem("Saucer", 1);
            default:
                if ("true".equals(System.getProperty("DEBUG_MODE"))) {
                    System.out.println("Unknown item type: " + itemType);
                }
                return null;
        }
    }
    
    /**
     * Render all visual items
     */
    public void paintItems(Graphics g) {
        for (VisualItem visualItem : visualItems) {
            visualItem.paint(g);
        }
    }
    
    /**
     * Check for item at specific cell
     */
    public VisualItem getItemAt(Cell cell) {
        for (VisualItem visualItem : visualItems) {
            if (visualItem.isAt(cell)) {
                return visualItem;
            }
        }
        return null;
    }
    
    /**
     * Remove an item from grid ( picked up)
     */
    public boolean removeItem(VisualItem visualItem) {
        return visualItems.remove(visualItem);
    }
    
   
    public Item removeItemAt(Cell cell) {
        Iterator<VisualItem> iterator = visualItems.iterator();
        while (iterator.hasNext()) {
            VisualItem visualItem = iterator.next();
            if (visualItem.isAt(cell)) {
                iterator.remove();
                return visualItem.getItem();
            }
        }
        return null;
    }
    //debugging
    public List<VisualItem> getAllItems() {
        return new ArrayList<>(visualItems);
    }
}