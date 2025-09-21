package items.consumables;

import actors.Dog;

/**
 * Dog-only food that requires a Bowl for consumption.
 */
public class Bone extends Consumable {
    
    public static final String REGISTRY_ID = "consumable.bone";
    public static final String REQUIRED_SERVEWARE = "tool.bowl";
    public static final int SIZE_UNITS = 2;
    public static final int BASE_EAT_MILLIS = 1500;
    
    /**
     * Creates a new Bone instance with standard properties.
     */
    public Bone() {
        super("Bone", SIZE_UNITS, Dog.class, BASE_EAT_MILLIS);
    }
    
    @Override
    public String getRegistryId() {
        return REGISTRY_ID;
    }
}
