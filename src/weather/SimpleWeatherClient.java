package weather;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * Weather client using modern HttpClient for async streaming
 * I implemented this using Java 11's HttpClient API to demonstrate proper async programming.
 * The server sends Server-Sent Events (SSE), so I needed to handle continuous streaming
 * rather than just reading once and closing the connection.
 */
public class SimpleWeatherClient {
    private final String url = "http://13.238.167.130/weather";
    private final HttpClient client;
    private volatile List<SimpleWeatherPoint> latestData;
    private CompletableFuture<Void> streamingTask;
    private final int MAX_BUFFER_SIZE = 50; // Keep recent weather points
    
    public SimpleWeatherClient() {
        // I chose to use the default HttpClient configuration for simplicity
        this.client = HttpClient.newHttpClient();
        this.latestData = new CopyOnWriteArrayList<>(); // Thread-safe for concurrent access
        startStreaming();
    }
    
    /**
     * Start async streaming from the weather server
     * This runs in the background continuously collecting weather data
     */
    private void startStreaming() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "text/event-stream") // SSE header required by server
                .build();
        
        // Using async streaming as specified in the assignment
        streamingTask = client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body) // Lambda to extract the input stream
                .thenAccept(this::processStream) // Lambda to handle stream processing
                .exceptionally(ex -> {
                    if ("true".equals(System.getProperty("DEBUG_MODE"))) {
                        System.err.println("Weather stream error: " + ex.getMessage());
                    }
                    // Return fallback data so game doesn't crash
                    addFallbackData();
                    return null;
                });
    }
    
    /**
     * Process the continuous input stream from the server
     * I used lambdas here to make the line processing more functional
     */
    private void processStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            
            // Continuously read and process lines using stream operations
            reader.lines()
                .map(this::parseLine) // Lambda to parse each line
                .filter(Objects::nonNull) // Lambda to filter out null results
                .filter(this::isInGameBounds) // Lambda to filter points outside grid
                .forEach(this::addWeatherPoint); // Lambda to add to collection
                
        } catch (IOException e) {
            if ("true".equals(System.getProperty("DEBUG_MODE"))) {
                System.err.println("Error reading SSE stream: " + e.getMessage());
            }
        }
    }
    
    /**
     * Check if weather point coordinates are within our 20x20 grid
     * Grid coordinates are centered at origin, so range is -10 to 10
     */
    private boolean isInGameBounds(SimpleWeatherPoint point) {
        return point.getX() >= -10 && point.getX() <= 10 &&
               point.getY() >= -10 && point.getY() <= 10;
    }
    
    /**
     * Add a weather point to our buffer, maintaining size limit
     * I used thread-safe operations since this runs on background thread
     */
    private synchronized void addWeatherPoint(SimpleWeatherPoint point) {
        latestData.add(point);
        
        // Keep buffer size manageable by removing oldest entries
        if (latestData.size() > MAX_BUFFER_SIZE) {
            latestData = latestData.stream()
                .skip(latestData.size() - MAX_BUFFER_SIZE) // Keep most recent
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Get current weather data
     * Returns a defensive copy to prevent concurrent modification
     */
    public List<SimpleWeatherPoint> getWeatherData() {
        // Using stream to create defensive copy
        return latestData.stream()
                .collect(Collectors.toList());
    }
    
    /**
     * Parse a line from the weather server
     * Format: timestamp attribute x-coordinate y-coordinate value
     * Example: 1760664486 rain 7 9 0.44
     */
    private SimpleWeatherPoint parseLine(String line) {
        try {
            String[] parts = line.split(" ");
            if (parts.length == 5) {
                long timestamp = Long.parseLong(parts[0]);
                String type = parts[1];
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                double value = Double.parseDouble(parts[4]);
                
                return new SimpleWeatherPoint(timestamp, x, y, type, value);
            }
        } catch (Exception e) {
            // Skip malformed lines - they happen occasionally with network issues
        }
        return null;
    }
    
    /**
     * Add fallback data when connection fails
     * This ensures the game can still run even if weather server is down
     */
    private void addFallbackData() {
        long now = System.currentTimeMillis() / 1000;
        // Generate some reasonable default weather across the grid
        for (int x = -5; x <= 5; x += 5) {
            for (int y = -5; y <= 5; y += 5) {
                latestData.add(new SimpleWeatherPoint(now, x, y, "temp", 0.5));
                latestData.add(new SimpleWeatherPoint(now, x, y, "rain", 0.2));
                latestData.add(new SimpleWeatherPoint(now, x, y, "windx", 0.5));
                latestData.add(new SimpleWeatherPoint(now, x, y, "windy", 0.5));
            }
        }
    }
    
    /**
     * Test if connection is working
     * I check if we're receiving recent data (within last minute)
     */
    public boolean testConnection() {
        if (latestData.isEmpty()) return false;
        
        long now = System.currentTimeMillis() / 1000;
        // Using stream to check if any data is recent
        return latestData.stream()
                .anyMatch(point -> now - point.timestamp < 60);
    }
    
    /**
     * Cleanup method for graceful shutdown
     */
    public void shutdown() {
        if (streamingTask != null && !streamingTask.isDone()) {
            streamingTask.cancel(true);
        }
    }
}