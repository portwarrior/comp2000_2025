package items;

import java.util.Objects;

/**
 * Focuses solely on carry mechanics without animal, terrain, or spawn concerns.
 */
public abstract class Item {
    private final String name;
    private final int sizeUnits;
    
    /**
     * Creates a new Item with the specified properties.
     * 
     * @param name Human-readable key for UI and logs (not identity)
     * @param sizeUnits Integer capacity cost (≥0)
     * @throws IllegalArgumentException if sizeUnits < 0
     */
    protected Item(String name, int sizeUnits) {
        if (sizeUnits < 0) {
            throw new IllegalArgumentException("sizeUnits must be >= 0, got: " + sizeUnits);
        }
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.sizeUnits = sizeUnits;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * return Integer capacity cost for inventory calculations
     */
    public int getSizeUnits() {
        return sizeUnits;
    }
    
    /**
     * value semantics equality includes concrete class + name + sizeUnits.
     * This ensures "Bone(2)" ≠ "Milk(2)" even with same size.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        
        Item item = (Item) obj;
        return sizeUnits == item.sizeUnits && 
               Objects.equals(name, item.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), name, sizeUnits);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{name='" + name + "', sizeUnits=" + sizeUnits + "}";
    }
    
    /**
     * return Registry ID for configuration and spawning systems
     */
    public abstract String getRegistryId();
}
