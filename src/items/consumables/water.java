package items.consumables;

import actors.Actor;

/**
 * Requires a Saucer for consumption by any animal type.
 */
public class Water extends Consumable {
    
    public static final String REGISTRY_ID = "consumable.water";
    public static final String REQUIRED_SERVEWARE = "tool.saucer";
    public static final int SIZE_UNITS = 1;
    public static final int BASE_EAT_MILLIS = 1000;
    
    /**
     * Creates a new Water instance with standard properties
     */
    public Water() {
        super("Water", SIZE_UNITS, Actor.class, BASE_EAT_MILLIS);
    }
    
    @Override
    public String getRegistryId() {
        return REGISTRY_ID;
    }
}
