package weather;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Weather manager that processes streaming weather data using lambdas and streams
 * I refactored this to demonstrate functional programming with the weather API
 */
public class SimpleWeatherManager {
    private SimpleWeatherClient client;
    private List<SimpleWeatherPoint> currentWeather;
    private long lastUpdate = 0;
    
    public SimpleWeatherManager() {
        client = new SimpleWeatherClient();
        updateWeather(); // Get initial data
    }
    
    /**
     * Update weather periodically
     * I chose 30 seconds to balance between freshness and server load
     */
    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > 30000) { // 30 seconds
            updateWeather();
            lastUpdate = now;
        }
    }
    
    /**
     * Fetch fresh weather data from the streaming client
     */
    private void updateWeather() {
        currentWeather = client.getWeatherData();
        if ("true".equals(System.getProperty("DEBUG_MODE"))) {
            System.out.println("Updated weather - got " + currentWeather.size() + " points");
        }
    }
    
    /**
     * Check if it's raining at a specific grid position
     * Using streams to filter and find rain data
     */
    public boolean isRainingAt(int x, int y) {
        double rainIntensity = getRainIntensityAt(x, y);
        return rainIntensity > 0.2; // I found 0.2 to be a good threshold through testing
    }
    
    /**
     * Get rain intensity using stream operations
     * This demonstrates filter + map + findFirst lambda chain
     */
    public double getRainIntensityAt(int x, int y) {
        if (currentWeather == null) return 0.0;
        
        return currentWeather.stream()
                .filter(point -> "rain".equals(point.getWeatherType())) // Lambda: filter rain
                .filter(point -> point.getX() == x && point.getY() == y) // Lambda: filter location
                .mapToDouble(SimpleWeatherPoint::getValue) // Method reference for mapping
                .findFirst()
                .orElse(0.0); // Default if no rain data found
    }
    
    /**
     * Get temperature at position using functional approach
     * I structured this similarly to rain for consistency
     */
    public double getTemperatureAt(int x, int y) {
        if (currentWeather == null) return 0.5; // Neutral default
        
        return currentWeather.stream()
                .filter(point -> "temp".equals(point.getWeatherType()))
                .filter(point -> point.getX() == x && point.getY() == y)
                .mapToDouble(SimpleWeatherPoint::getValue)
                .findFirst()
                .orElse(0.5);
    }
    
    /**
     * Get wind X component (horizontal wind)
     * I convert the 0-1 range to -1 to +1 for directional wind
     */
    public double getWindXAt(int x, int y) {
        if (currentWeather == null) return 0.0;
        
        return currentWeather.stream()
                .filter(point -> "windx".equals(point.getWeatherType()))
                .filter(point -> point.getX() == x && point.getY() == y)
                .mapToDouble(point -> (point.getValue() - 0.5) * 2) // Lambda: convert to -1 to +1
                .findFirst()
                .orElse(0.0);
    }
    
    /**
     * Get wind Y component (vertical wind)
     * Same conversion approach as windX for consistency
     */
    public double getWindYAt(int x, int y) {
        if (currentWeather == null) return 0.0;
        
        return currentWeather.stream()
                .filter(point -> "windy".equals(point.getWeatherType()))
                .filter(point -> point.getX() == x && point.getY() == y)
                .mapToDouble(point -> (point.getValue() - 0.5) * 2)
                .findFirst()
                .orElse(0.0);
    }
    
    /**
     * Calculate overall weather conditions across the entire game area
     * This uses advanced stream operations: grouping, mapping, and averaging
     */
    public WeatherConditions getOverallWeather() {
        if (currentWeather == null || currentWeather.isEmpty()) {
            return new WeatherConditions();
        }
        
        // Using streams to calculate averages for each weather type
        // I group by weather type then calculate average of values
        
        double avgRain = currentWeather.stream()
                .filter(p -> "rain".equals(p.getWeatherType()))
                .mapToDouble(SimpleWeatherPoint::getValue)
                .average()
                .orElse(0.0);
        
        double avgTemp = currentWeather.stream()
                .filter(p -> "temp".equals(p.getWeatherType()))
                .mapToDouble(SimpleWeatherPoint::getValue)
                .average()
                .orElse(0.5);
        
        double avgWindX = currentWeather.stream()
                .filter(p -> "windx".equals(p.getWeatherType()))
                .mapToDouble(p -> (p.getValue() - 0.5) * 2)
                .average()
                .orElse(0.0);
        
        double avgWindY = currentWeather.stream()
                .filter(p -> "windy".equals(p.getWeatherType()))
                .mapToDouble(p -> (p.getValue() - 0.5) * 2)
                .average()
                .orElse(0.0);
        
        return new WeatherConditions(avgRain, avgTemp, avgWindX, avgWindY);
    }
    
    /**
     * Get weather summary for display
     * Using streams to count different weather types
     */
    public String getWeatherSummary() {
        if (currentWeather == null || currentWeather.isEmpty()) {
            return "No weather data";
        }
        
        // Count how many data points we have by type using streams
        long rainPoints = currentWeather.stream()
                .filter(p -> "rain".equals(p.getWeatherType()))
                .count();
        
        long tempPoints = currentWeather.stream()
                .filter(p -> "temp".equals(p.getWeatherType()))
                .count();
        
        return String.format("Weather: %d rain, %d temp points", rainPoints, tempPoints);
    }
    
    /**
     * Test if weather system has live connection
     */
    public boolean isWorking() {
        return client.testConnection();
    }
    
    /**
     * Get all current weather points for a specific type
     * Useful for debugging and visualization
     */
    public List<SimpleWeatherPoint> getWeatherByType(String type) {
        if (currentWeather == null) return List.of();
        
        return currentWeather.stream()
                .filter(point -> type.equals(point.getWeatherType()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get maximum rain intensity across the map
     * Using stream max operation
     */
    public double getMaxRainIntensity() {
        if (currentWeather == null) return 0.0;
        
        OptionalDouble max = currentWeather.stream()
                .filter(p -> "rain".equals(p.getWeatherType()))
                .mapToDouble(SimpleWeatherPoint::getValue)
                .max();
        
        return max.orElse(0.0);
    }
    
    /**
     * Check if any location has extreme weather
     * I define extreme as rain > 0.8 or temp outside 0.2-0.8 range
     */
    public boolean hasExtremeWeather() {
        if (currentWeather == null) return false;
        
        boolean extremeRain = currentWeather.stream()
                .filter(p -> "rain".equals(p.getWeatherType()))
                .anyMatch(p -> p.getValue() > 0.8);
        
        boolean extremeTemp = currentWeather.stream()
                .filter(p -> "temp".equals(p.getWeatherType()))
                .anyMatch(p -> p.getValue() < 0.2 || p.getValue() > 0.8);
        
        return extremeRain || extremeTemp;
    }
    
    /**
     * Cleanup resources when shutting down
     */
    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }
}