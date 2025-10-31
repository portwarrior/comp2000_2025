/**
 * Debug utility class for controlling debug output and visual elements
 * Only shows debug information when DEBUG_MODE system property is set to true
 */
public class Debug {
    private static final boolean DEBUG_MODE = "true".equals(System.getProperty("DEBUG_MODE"));
    
    // Private constructor to prevent instantiation
    private Debug() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Check if debug mode is enabled
     */
    public static boolean isEnabled() {
        return DEBUG_MODE;
    }
    
    /**
     * Print debug message only if debug mode is enabled
     */
    public static void println(String message) {
        if (DEBUG_MODE) {
            System.out.println("[DEBUG] " + message); // NOSONAR - intentional console output for debugging
        }
    }
    
    /**
     * Print debug message with format only if debug mode is enabled
     */
    public static void printf(String format, Object... args) {
        if (DEBUG_MODE) {
            System.out.printf("[DEBUG] " + format + "%n", args); // NOSONAR - intentional console output for debugging
        }
    }
}