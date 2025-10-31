package weather;

/**
 * Represents a single weather data point from the server
 * I made this immutable by using public final fields - simpler than getters for data objects
 */
public class SimpleWeatherPoint {
    public final long timestamp;
    public final int x, y;
    public final String weatherType;
    public final double value;
    
    /**
     * Create a weather point with all necessary data
     * Coordinates use grid system where (0,0) is center
     */
    public SimpleWeatherPoint(long timestamp, int x, int y, String weatherType, double value) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.weatherType = weatherType;
        this.value = value;
    }
    
    /**
     * Human-readable representation for debugging
     * Format it to show the key info at a glance
     */
    public String toString() {
        return weatherType + "=" + value + " at (" + x + "," + y + ")";
    }
    
    // Getters for compatibility with stream operations
    public String getWeatherType() { return weatherType; }
    public double getValue() { return value; }
    public int getX() { return x; }
    public int getY() { return y; }
    public long getTimestamp() { return timestamp; }
}