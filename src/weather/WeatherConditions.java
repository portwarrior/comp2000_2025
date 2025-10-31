package weather;

/**
 * Comprehensive weather conditions data structure
 * Holds all weather parameters for rich weather effects
 */
public class WeatherConditions {
    public final double rainIntensity;    // 0.0 to 1.0
    public final double temperature;      // 0.0 (cold) to 1.0 (hot)
    public final double windX;           // -1.0 to 1.0 (west to east)
    public final double windY;           // -1.0 to 1.0 (north to south)
    
    // Default constructor - calm weather
    public WeatherConditions() {
        this(0.0, 0.5, 0.0, 0.0);
    }
    
    public WeatherConditions(double rainIntensity, double temperature, double windX, double windY) {
        this.rainIntensity = rainIntensity;
        this.temperature = temperature;
        this.windX = windX;
        this.windY = windY;
    }
    
    // Convenience methods
    public boolean isRaining() {
        return rainIntensity > 0.2;
    }
    
    public boolean isWindy() {
        return Math.abs(windX) > 0.3 || Math.abs(windY) > 0.3;
    }
    
    public boolean isCold() {
        return temperature < 0.3;
    }
    
    public boolean isHot() {
        return temperature > 0.7;
    }
    
    public double getWindSpeed() {
        return Math.sqrt(windX * windX + windY * windY);
    }
    
    public String getWeatherDescription() {
        StringBuilder desc = new StringBuilder();
        
        if (isRaining()) {
            if (rainIntensity > 0.7) desc.append("Heavy Rain");
            else if (rainIntensity > 0.4) desc.append("Rain");
            else desc.append("Light Rain");
        } else {
            desc.append("Clear");
        }
        
        if (isWindy()) {
            desc.append(", Windy");
        }
        
        if (isCold()) {
            desc.append(", Cold");
        } else if (isHot()) {
            desc.append(", Hot");
        }
        
        return desc.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Weather[Rain=%.2f, Temp=%.2f, Wind=(%.2f,%.2f)]", 
                           rainIntensity, temperature, windX, windY);
    }
}