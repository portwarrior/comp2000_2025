package items.consumables;

import items.Item;
import actors.Actor;
import java.util.Objects;

/**
 * Base for items typed, terrain-aware consumption mechanics
 */
public abstract class Consumable extends Item {
    private final Class<? extends Actor> targetType;
    private final boolean isUniversal;
    private final int baseEatMillis;
    
    /**
     * penalty applied when required gear is missing. defaulty is 45%
     */
    public static final double DEFAULT_PENALTY_FACTOR = 1.45;
    
    
    protected Consumable(String name, int sizeUnits, Class<? extends Actor> targetType, int baseEatMillis) {
        super(name, sizeUnits);
        
        if (baseEatMillis <= 0) {
            throw new IllegalArgumentException("baseEatMillis must be positive, got: " + baseEatMillis);
        }
        
        this.targetType = Objects.requireNonNull(targetType, "targetType cannot be null");
        this.isUniversal = (targetType == Actor.class);
        this.baseEatMillis = baseEatMillis;
        
        // Validate universal constraint
        if (isUniversal && targetType != Actor.class) {
            throw new IllegalArgumentException("Universal consumables must have targetType == Actor.class");
        }
    }
    
    /**
     * @return The Actor class allowed to consume this item
     */
    public Class<? extends Actor> getTargetType() {
        return targetType;
    }
    
    /**
     * consumables extend Item's equals to also include targetType and baseEatMillis.
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        
        Consumable other = (Consumable) obj;
        return baseEatMillis == other.baseEatMillis &&
               Objects.equals(targetType, other.targetType);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), targetType, baseEatMillis);
    }
    
    @Override
    public String toString() {
        return String.format("%s{name='%s', sizeUnits=%d, targetType=%s, baseEatMillis=%d}", 
                this.getClass().getSimpleName(), getName(), getSizeUnits(), 
                targetType.getSimpleName(), baseEatMillis);
    }
}
