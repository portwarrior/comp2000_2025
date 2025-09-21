package items.consumables;

import actors.Cat;

/**
 * Cat-only drink that requires a Cup for consumption.
 */
public class Milk extends Consumable {
    
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
}
