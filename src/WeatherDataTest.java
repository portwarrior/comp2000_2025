import weather.*;
import java.util.List;

public class WeatherDataTest {
    public static void main(String[] args) {
        System.out.println("Testing weather server data...");
        
        SimpleWeatherClient client = new SimpleWeatherClient();
        List<SimpleWeatherPoint> data = client.getWeatherData();
        
        System.out.println("Got " + data.size() + " weather points:");
        
        for (int i = 0; i < Math.min(20, data.size()); i++) {
            SimpleWeatherPoint point = data.get(i);
            System.out.println("Point " + i + ": " + point.toString());
        }
        
        // Analyze what weather types we have
        System.out.println("\nWeather types found:");
        for (SimpleWeatherPoint point : data) {
            System.out.println("Type: " + point.getWeatherType() + " Value: " + point.getValue() + " at (" + point.getX() + "," + point.getY() + ")");
        }
    }
}