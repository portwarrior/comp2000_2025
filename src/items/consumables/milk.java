package items.consumables;

import actors.Actor;
import actors.Cat;

/**
 * Cat-only drink that requires a Cup for consumption.
 */
public class Milk extends Consumable implements RequiresServeware {
    
    public static final String REGISTRY_ID = "consumable.milk";
    public static final String REQUIRED_SERVEWARE = "tool.cup";
    public static final int SIZE_UNITS = 2;
    public static final int BASE_EAT_MILLIS = 1500;
    
    /**
     * Creates a new Milk instance with standard properties.
     */
    public Milk() {
        super("Milk", SIZE_UNITS, Cat.class, BASE_EAT_MILLIS);
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
     * Milk is allowed in depths terrain.
     * Cat entry to depths is gated by TerrainPolicy (must wear SwimSuit)
     * and may incur +45% consumption penalty.
     */
    @Override
    public boolean allowedInDepths() {
        return true;
    }
}
