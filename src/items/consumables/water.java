package items.consumables;

import actors.Actor;

/**
 * Requires a Saucer for consumption by any animal type.
 */
public class Water extends Consumable implements RequiresServeware {
    
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
    
    @Override
    public String requiredServewareToken() {
        return REQUIRED_SERVEWARE;
    }
    
    /**
     * Water is allowed in all terrains including depths.
     * All animals can access water but need appropriate gear for  specific terrain
     */
    @Override
    public boolean allowedInDepths() {
        return true;
    }
}
