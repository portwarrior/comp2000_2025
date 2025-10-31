package patterns;

import actors.Actor;
import actors.Cat;
import actors.Dog;
import actors.Bird;

/**
 * Strategy pattern for animal weather responses
 * Different animal types have different behavioral reactions to weather conditions
 */
public class SimpleWeatherStrategy implements WeatherStrategy {
    private String animalType;
    
    public SimpleWeatherStrategy(String animalType) {
        this.animalType = animalType;
    }
    
    @Override
    public void respondToWeather(Actor animal, boolean isRaining, double temperature) {
        // Determine animal type based on class
        String actualType = animal.getClass().getSimpleName();
        
        if ("Cat".equals(actualType)) {
            // Cats hate rain and cold
            if (isRaining) {
                animal.takeDamage(5);
                // Add some dramatic flair - cats really hate getting wet!
                if (Math.random() < 0.3) {
                    animal.takeDamage(2); // Extra damage from stress
                }
            }
            if (temperature < 0.3) {
                animal.takeDamage(3);
                // Cats seek warmth desperately when cold
                if (temperature < 0.2) {
                    animal.takeDamage(2); // Hypothermia risk
                }
            }
            if (!isRaining && temperature >= 0.6 && temperature <= 0.8) {
                animal.heal(1);
                // Cats love sunbathing in perfect weather
                if (temperature >= 0.7 && temperature <= 0.75) {
                    animal.heal(1); // Extra healing in ideal conditions
                }
            }
        } else if ("Dog".equals(actualType)) {
            // Dogs like rain but hate heat
            if (isRaining) {
                animal.heal(2);
                // Dogs enjoy playing in puddles
                if (Math.random() < 0.2) {
                    animal.heal(1); // Joy bonus!
                }
            }
            if (temperature > 0.8) {
                animal.takeDamage(4);
                // Dogs pant heavily in extreme heat
                if (temperature > 0.9) {
                    animal.takeDamage(3); // Heat exhaustion
                }
            }
            if (temperature < 0.2) {
                animal.takeDamage(2);
                // Even dogs can get too cold
            }
        } else if ("Bird".equals(actualType)) {
            // Birds are very sensitive to weather
            if (isRaining) {
                animal.takeDamage(7);
                // Wet feathers are dangerous for birds
                if (temperature < 0.5) {
                    animal.takeDamage(3); // Cold rain is deadly
                }
            }
            if (temperature < 0.4 || temperature > 0.7) {
                animal.takeDamage(5);
                // Birds need very specific temperature ranges
                if (temperature < 0.2 || temperature > 0.9) {
                    animal.takeDamage(5); // Extreme temperatures are lethal
                }
            }
            // Birds love perfect flying weather
            if (!isRaining && temperature >= 0.5 && temperature <= 0.6) {
                animal.heal(2); // Ideal flying conditions
            }
        }
    }
    
    @Override
    public int getUrgencyLevel(boolean isRaining, double temperature) {
        if ("Cat".equals(animalType)) {
            if (isRaining) return 8;
            if (temperature < 0.2) return 6;
            if (temperature > 0.9) return 4;
            return 2;
        } else if ("Dog".equals(animalType)) {
            if (temperature > 0.8) return 7;
            if (temperature < 0.2) return 3;
            if (isRaining) return 1; // Dogs like rain
            return 2;
        } else if ("Bird".equals(animalType)) {
            if (isRaining) return 9;
            if (temperature < 0.4) return 10;
            if (temperature > 0.7) return 8;
            return 3;
        }
        return 5; // Default urgency
    }
    
    @Override
    public String getWeatherResponse(boolean isRaining, double temperature) {
        if ("Cat".equals(animalType)) {
            if (isRaining && temperature < 0.3) {
                return "hiding from cold rain, very distressed!";
            } else if (isRaining) {
                return "seeking shelter from rain, looking miserable";
            } else if (temperature < 0.2) {
                return "shivering in the cold, needs warmth";
            } else if (temperature >= 0.7 && temperature <= 0.75) {
                return "purring contentedly in the perfect weather";
            } else if (!isRaining && temperature >= 0.6) {
                return "enjoying the warm sunshine";
            }
            return "seems comfortable";
        } else if ("Dog".equals(animalType)) {
            if (isRaining && temperature > 0.5) {
                return "playing happily in the rain!";
            } else if (isRaining) {
                return "enjoying the rain despite the cold";
            } else if (temperature > 0.9) {
                return "panting heavily, overheating!";
            } else if (temperature > 0.8) {
                return "looking for shade, too hot";
            } else if (temperature < 0.2) {
                return "whimpering from the cold";
            }
            return "wagging tail, content";
        } else if ("Bird".equals(animalType)) {
            if (isRaining && temperature < 0.5) {
                return "desperately seeking shelter, feathers soaked!";
            } else if (isRaining) {
                return "struggling with wet feathers";
            } else if (temperature < 0.2) {
                return "huddled for warmth, very cold";
            } else if (temperature > 0.9) {
                return "panting with beak open, overheated";
            } else if (!isRaining && temperature >= 0.5 && temperature <= 0.6) {
                return "soaring gracefully in perfect conditions";
            }
            return "alert and watching the sky";
        }
        return "monitoring weather conditions";
    }

    @Override
    public String getAnimalType() {
        return animalType;
    }
}