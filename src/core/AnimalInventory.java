package core;

import items.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * inventory system for actors
 */
public class AnimalInventory {
    
    private List<Item> items;
    private int maxCapacity;
    
    public AnimalInventory(int capacity) {
        this.items = new ArrayList<>();
        this.maxCapacity = capacity;
    }
    
    /**
     * Add an item to inventory if there's space
     */
    public boolean addItem(Item item) {
        if (getCurrentSize() + item.getSizeUnits() <= maxCapacity) {
            items.add(item);
            return true;
        }
        return false; // Not enough space
    }
    
    /**
     * Remove an item from inventory
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }
    
    /**
     * Get all items of a specific type
     */
    public List<Item> getItemsByType(Class<? extends Item> itemType) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (itemType.isInstance(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    /**
     * Check if inventory contains a specific item type
     */
    public boolean hasItemType(Class<? extends Item> itemType) {
        return !getItemsByType(itemType).isEmpty();
    }
    
    /**
     * Get current total size used
     */
    public int getCurrentSize() {
        int total = 0;
        for (Item item : items) {
            total += item.getSizeUnits();
        }
        return total;
    }
    
    /**
     * Get remaining capacity
     */
    public int getRemainingCapacity() {
        return maxCapacity - getCurrentSize();
    }
    
    /**
     * Get all items
     */
    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }
    
    /**
     * Get inventory summary for display
     */
    public String getInventorySummary() {
        if (items.isEmpty()) {
            return "Empty";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("Items: ");
        
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) summary.append(", ");
            summary.append(items.get(i).getName());
        }
        
        summary.append(" (").append(getCurrentSize()).append("/").append(maxCapacity).append(")");
        return summary.toString();
    }
    
    /**
     * Clear all items
     */
    public void clear() {
        items.clear();
    }
    
    /**
     * Check if inventory is full
     */
    public boolean isFull() {
        return getCurrentSize() >= maxCapacity;
    }
    
    /**
     * Check if inventory is empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}