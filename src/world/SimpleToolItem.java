package world;

import items.Item;

/**
 * item for bowls, cups, and saucers
 * Temporary class until proper tool system is implemented
 */
public class SimpleToolItem extends Item {
    
    public SimpleToolItem(String name, int sizeUnits) {
        super(name, sizeUnits);
    }
    
    @Override
    public String getRegistryId() {
        return "tool." + getName().toLowerCase();
    }
}