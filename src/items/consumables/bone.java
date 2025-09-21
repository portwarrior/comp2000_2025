package items.consumables;

import actors.Actor;
import actors.Dog;

/**
 * Dog-only food that requires a Bowl for consumption.
 */
public class Bone extends Consumable implements RequiresServeware {
    
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
    
    @Override
    public String requiredServewareToken() {
        return REQUIRED_SERVEWARE;
    }
    
    /**
     * Bones allowed in all terrains
     * Dogs need special gear for certain terrains
     */
    @Override
    public boolean allowedInDepths() {
        return true;
    }
}
