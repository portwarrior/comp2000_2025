package items.consumables;

import actors.Bird;

/**
 * Bird-only food that requires no serveware cannot spawn in depths terrain as birds
 */
public class Worm extends Consumable {
    
    public static final String REGISTRY_ID = "consumable.worm";
    public static final int SIZE_UNITS = 1;
    public static final int BASE_EAT_MILLIS = 1200;
    
    /**
     * Creates a new Worm instance with standard properties.
     */
    public Worm() {
        super("Worm", SIZE_UNITS, Bird.class, BASE_EAT_MILLIS);
    }
    
    @Override
    public String getRegistryId() {
        return REGISTRY_ID;
    }
}
