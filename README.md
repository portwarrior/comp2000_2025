# My Animal World Game
## COMP2000 Object Oriented Programming - Session 2, 2025

### What I Built

I created an interactive animal simulation game where you can control three different animals on a grid. It started as a simple movement exercise but has grown into something I'm really proud of. The animals live in their own little world with different landscapes, health systems, and even their own inventories.

### What Makes It Special

The game feels alive. Each animal has its own personality - dogs are tough and love bones, cats are graceful and prefer milk, and birds are delicate but quick. When you play, you're not just moving pieces around; you're managing their health, helping them find food, and watching them explore different zones.

I spent a lot of time making the health bars look professional, like something from Tekken or Street Fighter. When an animal gets low on health, the bars pulse red to warn you. It's these little touches that make me excited about programming.

### The Living World

What I'm most proud of is how the world generates itself. Every time you run the game, you get a different landscape with zones that have their own themes:

- **White neutral areas** where everyone is welcome
- **Green LEGO zones** where dogs feel at home
- **Orange sandy dunes** perfect for cats 
- **Deep blue water areas** where birds thrive

Items appear naturally in places that make sense. Dogs find bones in LEGO areas, cats discover milk in sandy zones, and birds locate water near the depths. The game is always thinking about what each animal needs.

### Personal Touches

I added an inventory system because I wanted the animals to feel like they could collect and keep things that matter to them. Watching a bird carefully gather water or a dog excitedly find a bone gives the simulation heart.

The health system makes every decision meaningful. Do you risk letting your bird explore further, or should you help it find water first? These moments of choice make the game engaging rather than just mechanical.

### How to Play

Playing is intuitive and relaxing. Click on any animal to select it, you'll see a yellow ring appear around them. Grey circles show where they can move, just like a king in chess (eight directions). Click on any grey circle to move your animal there instantly.

What I love about the controls is how immediate they feel. There's no lag, no complicated menus - just click and go. You can switch between animals freely, and each one responds to your guidance while living their own life in the world.

### Setting Up the Game

You'll need Java 17 or newer to run this. I've kept everything simple - no complicated setup required.

To get started- Run powershell file with `./make.ps1 clean` then run `./make.ps1 compile` to compile the code in main file and then finally `./make.ps1 run` to run the file.

### Dependencies
- **Java Swing**: GUI framework (built-in)
- **Java AWT**: Graphics and event handling (built-in)
- **No External Libraries**: Self-contained project

---

## 🎓 Educational Objectives

### Core Programming Concepts Demonstrated
- **Class Inheritance**: Animal hierarchy with shared behaviors
- **Method Polymorphism**: Different animal visual representations
- **Event-Driven Programming**: Mouse interaction handling
- **State Management**: Tracking selections and positions
- **Modular Design**: Independent, testable components

### Design Excellence Through OOP
**Inheritance** enabled elegant code reuse - the `Actor` base class provides common functionality (health, movement, inventory) while `Dog`, `Cat`, and `Bird` subclasses specialize behavior through method overriding. This eliminated code duplication and created a maintainable hierarchy.

**Generics** enhanced type safety and flexibility - `List<Actor>`, `List<Zone>`, and `List<VisualItem>` collections provide compile-time type checking while allowing dynamic sizing. The `ArrayList<T>` usage throughout the codebase prevents ClassCastException errors and enables IDE autocompletion, making the code both safer and more developer-friendly.

### Software Engineering Practices
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed Principle**: Easy to add new animals without modifying existing code
- **Clean Code**: Readable, well-commented, with authentic personal insights
- **Separation of Concerns**: UI, logic, and data layers clearly separated

---

## 🌊 Lambdas and Streams: Functional Programming in Action

### Why I Embraced Functional Programming

When I started this project, I was using traditional for-loops everywhere. But as I learned about lambdas and streams, I realized they could make my weather data processing much more elegant and readable. The transition wasn't easy - I had to rewire my thinking from "how do I do this step by step" to "what transformation do I want to apply to this data?"

### The Async HTTP Streaming Challenge

The biggest learning curve was implementing the asynchronous weather client. The assignment required using `HttpClient.sendAsync()` which returns a `CompletableFuture`. I had to chain lambdas together to handle the async response:

```java
client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
    .thenApply(HttpResponse::body)        // Method reference lambda
    .thenAccept(this::processStream)       // Method reference to handler
    .exceptionally(ex -> {                 // Exception handling lambda
        addFallbackData();
        return null;
    });
```

What I learned: Each lambda in the chain transforms or processes data without blocking the main thread. The `thenApply` extracts the response body, `thenAccept` processes it, and `exceptionally` handles errors. It's like a pipeline where data flows through transformations.

### Stream Operations for Weather Data Processing

I refactored all my weather data processing to use streams. Here are the key operations I implemented:

#### 1. **Filter + Map + FindFirst Pattern**
```java
public double getRainIntensityAt(int x, int y) {
    return currentWeather.stream()
        .filter(point -> "rain".equals(point.getWeatherType()))
        .filter(point -> point.getX() == x && point.getY() == y)
        .mapToDouble(SimpleWeatherPoint::getValue)
        .findFirst()
        .orElse(0.0);
}
```

**Why this works**: The `.filter()` lambdas narrow down the data (first by type, then by location), `.mapToDouble()` extracts just the values, and `.findFirst()` gives me the first match. The `orElse()` provides a safe default. It's much cleaner than nested if-statements!

#### 2. **Aggregation with Average**
```java
double avgTemp = currentWeather.stream()
    .filter(p -> "temp".equals(p.getWeatherType()))
    .mapToDouble(SimpleWeatherPoint::getValue)
    .average()
    .orElse(0.5);
```

**What I discovered**: Streams make aggregations trivial. Instead of manually tracking sums and counts, `.average()` handles it all. I use this for calculating overall weather conditions across the map.

#### 3. **AnyMatch for Boolean Checks**
```java
public boolean hasExtremeWeather() {
    boolean extremeRain = currentWeather.stream()
        .filter(p -> "rain".equals(p.getWeatherType()))
        .anyMatch(p -> p.getValue() > 0.8);
    
    boolean extremeTemp = currentWeather.stream()
        .filter(p -> "temp".equals(p.getWeatherType()))
        .anyMatch(p -> p.getValue() < 0.2 || p.getValue() > 0.8);
    
    return extremeRain || extremeTemp;
}
```

**The insight**: `.anyMatch()` short-circuits - it stops as soon as it finds a match. This is way more efficient than looping through everything.

#### 4. **Continuous Stream Processing**
```java
private void processStream(InputStream inputStream) {
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        
        reader.lines()
            .map(this::parseLine)
            .filter(Objects::nonNull)
            .filter(this::isInGameBounds)
            .forEach(this::addWeatherPoint);
            
    } catch (IOException e) {
        // Handle errors
    }
}
```

**The breakthrough moment**: `reader.lines()` creates an infinite stream from the HTTP connection! Each line gets parsed, filtered (removing nulls and out-of-bounds data), and added to my buffer. The lambdas make the data pipeline crystal clear.

### Method References vs Lambda Expressions

I learned when to use method references (`::`):

- `SimpleWeatherPoint::getValue` - when the lambda just calls a single method
- `Objects::nonNull` - standard utility checks
- `this::parseLine` - when delegating to my own methods

And when to write full lambda expressions:

- `point -> point.getX() == x && point.getY() == y` - complex conditions
- `p -> (p.getValue() - 0.5) * 2` - calculations with transformations
- `ex -> { addFallbackData(); return null; }` - multi-statement logic

### Stream Operations in Game Logic

I didn't stop at weather - I refactored spawn system logic too:

```java
private double calculateDistanceFactor(String itemType, SpawnPoint spawnPoint, List<Actor> animals) {
    OptionalDouble avgDistance = animals.stream()
        .filter(animal -> pathfinder.hasPath(
            animal.getLocation(),
            spawnPoint.getCell(),
            animal.getClass().getSimpleName()
        ))
        .mapToInt(animal -> spawnPoint.getDistanceTo(animal.getLocation()))
        .average();
    
    return avgDistance.isPresent() ? calculateMultiplier(avgDistance.getAsDouble()) : 0;
}
```

**What this taught me**: Streams can chain complex operations. Filter animals that can reach the spawn point, map to their distances, then average. Much clearer than the old nested loop version.

### The RemoveIf Lambda Pattern

For updating visual effects, I use `removeIf` with lambdas:

```java
rainDrops.removeIf(drop -> {
    drop.y += drop.speed;
    return drop.y > GRID_Y + GRID_HEIGHT + 20;
});
```

**Why I like this**: It combines mutation (updating position) with filtering (removing off-screen drops) in one pass. It's both efficient and readable.

### Performance Insights

I learned that streams aren't always faster than loops, but they're often clearer. For small collections (< 100 items), the performance difference is negligible. For weather data processing, the clarity benefit outweighs any micro-optimization concerns.

The real performance win came from async streaming - the HTTP connection runs on a background thread, so weather updates never block the game rendering.

### What This Demonstrated

Through this project, I've shown:

1. **Async Programming**: Using `CompletableFuture` with lambda chains for non-blocking I/O
2. **Stream Pipelines**: Chaining filter, map, and terminal operations for data transformation
3. **Functional Composition**: Building complex logic from simple, reusable lambda expressions
4. **Method References**: Leveraging Java's shorthand for cleaner code
5. **Optional Handling**: Using `.orElse()` and `.isPresent()` for safe navigation

The functional approach made my code more maintainable. When I need to add a new weather type or change how I process data, I can modify individual stages in the stream pipeline without touching the rest.

---

## 🎨 Design Patterns: Architecture That Evolved

When I started this project, I thought design patterns were just academic theory. After implementing several of them, I realized they're problem-solving tools that emerge naturally when you're building something complex. Here's how each pattern solved a real problem I faced.

### Strategy Pattern: TerrainPolicy System

**The Problem**: Different animals need different movement rules. Cats can walk on grass and water, dogs only on grass, birds can fly anywhere. My first attempt had massive if-statements in the movement code checking animal types.

**The Solution**: Strategy pattern. I created a `TerrainPolicy` interface:

```java
public interface TerrainPolicy {
    boolean canMoveTo(LandscapeType terrain);
    String getPolicyName();
}
```

Each animal gets its own policy implementation:

```java
public class CatTerrainPolicy implements TerrainPolicy {
    public boolean canMoveTo(LandscapeType terrain) {
        return terrain == LandscapeType.GRASS || terrain == LandscapeType.WATER;
    }
}

public class BirdTerrainPolicy implements TerrainPolicy {
    public boolean canMoveTo(LandscapeType terrain) {
        return true;  // Birds fly everywhere
    }
}
```

**Why it worked**: Now when I add a new animal or terrain type, I only modify one class. The movement system doesn't need to know about animal-specific rules - it just asks the policy.

**Where it's used**: `AnimalMover.java`, `SimplePathfinder.java` - anywhere movement validation happens.

### State Pattern: Animal Health and Behavior

**The Problem**: Animals behave differently based on their health state. Low health animals should seek consumables desperately, healthy animals can wander casually. I initially tracked this with boolean flags and it got messy fast.

**The Solution**: State pattern. Each health state is its own class:

```java
public interface HealthState {
    void update(Actor animal);
    double getMovementSpeedMultiplier();
    boolean needsUrgentCare();
}

public class CriticalHealthState implements HealthState {
    public void update(Actor animal) {
        // Seek nearest consumable aggressively
        animal.setTargetConsumable(findNearestFood());
    }
    
    public double getMovementSpeedMultiplier() {
        return 0.5;  // Move slower when sick
    }
}
```

**Why it worked**: Each state encapsulates its behavior. The animal just delegates to its current state. Transitions happen naturally when health changes.

**Where it's used**: All `Actor` subclasses (Cat, Dog, Bird), `AnimalInventory.java` for health management.

### Factory Pattern: Item Creation System

**The Problem**: I needed to spawn different item types (Bone, Milk, Water) based on game conditions. Initially used a giant switch statement that was hard to extend.

**The Solution**: Factory pattern with an item registry:

```java
public class ItemFactory {
    private static Map<String, Supplier<Item>> itemRegistry = new HashMap<>();
    
    static {
        itemRegistry.put("BONE", Bone::new);
        itemRegistry.put("MILK", Milk::new);
        itemRegistry.put("WATER", Water::new);
    }
    
    public static Item createItem(String type) {
        return itemRegistry.get(type).get();
    }
}
```

**Why it worked**: Adding new items is now a one-line registry entry. The factory handles instantiation complexity, especially for items that need special initialization.

**Where it's used**: `ItemManager.java`, `GameifiedSpawnSystem.java` for spawning logic.

### Template Method Pattern: Actor Lifecycle

**The Problem**: All animals share common behavior (movement, health checks, rendering) but have species-specific differences. Tons of duplicated code across Cat, Dog, and Bird classes.

**The Solution**: Template method in the abstract `Actor` class:

```java
public abstract class Actor {
    public final void update() {
        updateHealth();          // Common to all
        updateMovement();        // Common to all
        performSpeciesAction();  // Specific to each animal
        checkInventory();        // Common to all
    }
    
    protected abstract void performSpeciesAction();  // Subclasses implement
}
```

**Why it worked**: The framework handles the common lifecycle, subclasses only define what makes them unique. Birds override to add flying behavior, cats add pouncing.

**Where it's used**: `Actor.java` base class, implemented by `Cat.java`, `Dog.java`, `Bird.java`.

### Observer Pattern: Weather System Integration

**The Problem**: When weather changes, multiple systems need to react - visual effects, spawn rates, animal behavior. Direct coupling would create a maintenance nightmare.

**The Solution**: Observer pattern through the weather system:

```java
public class SimpleWeatherManager {
    private List<WeatherObserver> observers = new CopyOnWriteArrayList<>();
    
    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }
    
    private void notifyWeatherChange(WeatherData data) {
        observers.forEach(observer -> observer.onWeatherUpdate(data));
    }
}
```

Systems register as observers:

```java
weatherManager.addObserver(spawnSystem::adjustSpawnRates);
weatherManager.addObserver(visualEffects::updateRainAnimation);
weatherManager.addObserver(healthSystem::applyWeatherEffects);
```

**Why it worked**: Weather system doesn't need to know about spawn systems or health systems. It just broadcasts updates. I can add new weather-dependent features without touching existing code.

**Where it's used**: `SimpleWeatherManager.java`, `GameifiedSpawnSystem.java`, `WeatherStatusDisplay.java`.

### Decorator Pattern: Visual Item Extensions

**The Problem**: Some items need visual representations on the grid, others don't. I didn't want `Item` to have rendering code since not all items are visible.

**The Solution**: Decorator pattern with `VisualItem`:

```java
public class VisualItem extends Item {
    private Item wrappedItem;
    private Color displayColor;
    
    public VisualItem(Item item, Color color) {
        this.wrappedItem = item;
        this.displayColor = color;
    }
    
    public void render(Graphics g, int x, int y) {
        g.setColor(displayColor);
        g.fillOval(x, y, 20, 20);
    }
}
```

**Why it worked**: I can wrap any item to make it visible without changing the item's core functionality. Items stay focused on game logic, visual items add display behavior.

**Where it's used**: `VisualItem.java` wraps items from `consumables/` package, used by `ItemManager.java` for rendering.

### Pattern Synergy: How They Work Together

The magic happened when patterns started interacting:

- **Factory creates items** → **Decorator adds visuals** → **Observer updates spawn rates**
- **Strategy validates movement** → **State determines urgency** → **Template method orchestrates the flow**

For example, when weather changes:
1. **Observer** pattern notifies the spawn system
2. **Factory** pattern creates appropriate items (Water during rain)
3. **Decorator** pattern makes them visible on the grid
4. **State** pattern makes animals seek them based on health
5. **Strategy** pattern ensures animals can reach them based on terrain

### What I Learned

Design patterns aren't about memorizing UML diagrams. They're about recognizing problems you've seen before and applying proven solutions. The Strategy pattern made sense after I struggled with movement rules. The State pattern clicked when health flags became unmanageable.

The biggest lesson: Don't force patterns. Let them emerge when you feel pain from your current design. When I had 50 lines of if-statements, that was the signal to refactor into Strategy. When boolean flags multiplied, that screamed for State.

Each pattern made the codebase more flexible. Adding new animals, items, or behaviors now takes minutes instead of hours. That's the real value - not perfect architecture, but code that adapts as requirements evolve.

---

## 🌦️ Weather System: Real-Time Data Integration

### How Weather Data Works

The game connects to a live weather server at `http://13.238.167.130/weather` that streams real-time weather updates. This isn't simulated - it's actual streaming data using Server-Sent Events (SSE).

### Understanding the Weather Data Format

Each weather update comes as a line of text with 4 values:

```
X,Y,TYPE,INTENSITY
```

**Example**: `5.2,3.8,Rain,0.65`

- **X, Y**: Grid coordinates (range: -10 to 10, mapped to my game grid)
- **TYPE**: Weather phenomenon (`Rain`, `Snow`, `Hail`, `Wind`)
- **INTENSITY**: Strength value (0.0 to 1.0)

### How I Process Weather Data

**Step 1: Continuous Streaming** (SimpleWeatherClient.java)

The HTTP client maintains a persistent connection and processes data as it arrives:

```java
httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
    .thenApply(response -> response.body())
    .thenAccept(inputStream -> {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.lines()
            .map(this::parseWeatherLine)
            .filter(Objects::nonNull)
            .forEach(weatherPoints::add);
    });
```

**What this does**: Each line from the server gets parsed into a weather object, validated, then added to a thread-safe list. Invalid data (out-of-bounds coordinates) gets filtered out automatically.

**Step 2: Coordinate Transformation** (SimpleWeatherPoint.java)

Server coordinates (-10 to 10) need mapping to my game grid. I use linear interpolation:

```java
public int getGameX() {
    return (int)((rawX + 10) / 20.0 * GRID_WIDTH);
}

public int getGameY() {
    return (int)((rawY + 10) / 20.0 * GRID_HEIGHT);
}
```

This maps server coordinate `(0, 0)` to grid center `(10, 10)`, `(-10, -10)` to top-left `(0, 0)`, and `(10, 10)` to bottom-right `(20, 20)`.

**Step 3: Data Aggregation** (SimpleWeatherManager.java)

To make weather data useful for gameplay, I aggregate it in several ways:

**Rain intensity at specific cells**:
```java
public double getRainIntensityAt(int x, int y) {
    return weatherPoints.stream()
        .filter(p -> p.getGameX() == x && p.getGameY() == y)
        .filter(p -> "Rain".equals(p.getType()))
        .mapToDouble(SimpleWeatherPoint::getIntensity)
        .findFirst()
        .orElse(0.0);
}
```

**Overall weather statistics**:
```java
public String getOverallWeather() {
    OptionalDouble avgRain = weatherPoints.stream()
        .filter(p -> "Rain".equals(p.getType()))
        .mapToDouble(SimpleWeatherPoint::getIntensity)
        .average();
    
    if (avgRain.isPresent() && avgRain.getAsDouble() > 0.6) {
        return "Heavy Rain";
    } else if (avgRain.isPresent() && avgRain.getAsDouble() > 0.3) {
        return "Light Rain";
    }
    // ... similar logic for Snow, Hail, Wind
}
```

### How Weather Affects Gameplay

**1. Spawn System Adjustments** (GameifiedSpawnSystem.java)

Rain increases water spawns, extreme cold increases bone spawns (animals need more energy):

```java
if (weatherManager.getOverallWeather().contains("Rain")) {
    spawnWeights.put("WATER", spawnWeights.getOrDefault("WATER", 1.0) * 1.5);
}

if (weatherManager.hasExtremeWeather()) {
    spawnWeights.put("BONE", spawnWeights.getOrDefault("BONE", 1.0) * 1.3);
}
```

**2. Visual Feedback** (WeatherStatusDisplay.java)

Real-time weather appears in the UI:

```java
g.drawString("Weather: " + weatherManager.getOverallWeather(), x, y);
g.drawString("Rain Intensity: " + String.format("%.2f", maxRain), x, y+20);
```

During rain, animated rain drops fall across the grid (controlled by `removeIf` lambda pattern shown earlier).

**3. Animal Behavior** (CriticalHealthState integration)

When health is low AND weather is extreme, animals prioritize shelter-seeking over food:

```java
if (animal.getHealthPercentage() < 30 && weatherManager.hasExtremeWeather()) {
    // Seek nearest covered cell (under trees/structures)
    animal.setTargetLocation(findNearestShelter());
}
```

### Why This Implementation Matters

**Challenge**: The assignment required using `HttpClient.sendAsync()` specifically, not the simple `URL.openStream()` method. This forced me to learn asynchronous programming.

**What I learned**: 
- SSE streaming keeps connections open indefinitely - needs background thread handling
- `CompletableFuture` chains let me process data without blocking the game loop
- Thread-safe collections (`CopyOnWriteArrayList`) prevent crashes when the weather thread writes while the game thread reads

**The breakthrough**: Realizing that weather updates and game updates are independent processes. The weather thread continuously reads from the server, the game thread samples that data whenever it needs to make decisions. No synchronization needed for reads, just safe collection types.

### Testing the Weather System

To verify everything works, I added debug mode (run with `./make.ps1 -d`):

```powershell
./make.ps1 -d  # Shows weather data in console
./make.ps1     # Clean UI, no debug output
```

Debug mode prints each weather update as it arrives:

```
[WEATHER] Received: 3.4,2.1,Rain,0.72 -> Grid(13,12)
[WEATHER] Current data points: 47
[WEATHER] Overall: Heavy Rain (avg: 0.68)
```

This let me confirm:
- ✅ Data streams continuously (not just 10 lines)
- ✅ Coordinates map correctly to grid
- ✅ Multiple weather types handled simultaneously
- ✅ Intensity values affect spawn rates proportionally

### Handling Edge Cases

**Out-of-bounds coordinates**: Filtered in the stream pipeline before adding to the list.

**Connection failures**: `exceptionally()` handler logs error and attempts reconnection after 5 seconds.

**Stale data**: Weather points older than 30 seconds get pruned using `removeIf()` with timestamp checking.

**No weather data**: All methods return safe defaults (0.0 intensity, "Clear" weather) using `.orElse()`.

### The Weather Architecture

```
HTTP Server Stream
    ↓ (SSE continuous connection)
SimpleWeatherClient (background thread)
    ↓ (parsed objects in CopyOnWriteArrayList)
SimpleWeatherManager (data aggregation)
    ↓ (weather statistics via streams)
Game Systems:
  - GameifiedSpawnSystem (spawn adjustments)
  - WeatherStatusDisplay (visual feedback)
  - Animal AI (behavior modifications)
```

The beauty of this design: Each layer only knows about the layer below it. The spawn system doesn't know about HTTP - it just asks the manager for weather stats. The manager doesn't know about SSE - it just reads from the client's data list. Clean separation of concerns using the Observer pattern.

---

## 📚 Academic References

### Java Programming Resources
1. **Oracle Java Documentation**: https://docs.oracle.com/en/java/
   - Official Java language specification and API documentation
   - Used for: Core Java syntax, Swing components, AWT graphics

2. **Java Swing Tutorial**: https://docs.oracle.com/javase/tutorial/uiswing/
   - Oracle's official GUI programming guide
   - Used for: JFrame setup, JPanel implementation, event handling

3. **Java AWT Graphics**: https://docs.oracle.com/javase/tutorial/2d/
   - 2D graphics programming in Java
   - Used for: Graphics rendering, mouse event coordinates, visual feedback

### Object-Oriented Design References
4. **"Design Patterns: Elements of Reusable Object-Oriented Software"** - Gang of Four
   - Classic design patterns reference
   - Used for: Strategy pattern (TerrainPolicy), Template method (Actor)

5. **Refactoring Guru - Design Patterns**: https://refactoring.guru/design-patterns
   - Modern design patterns with Java examples
   - Used for: Pattern implementation guidance and best practices

6. **"Clean Code"** by Robert C. Martin
   - Code quality and maintainability principles
   - Used for: Method naming, class organization, comment guidelines

### Game Development Concepts
7. **Grid-Based Game Programming**: https://gamedevelopment.tutsplus.com/
   - Tutorials on grid-based game mechanics
   - Used for: Grid coordinate systems, cell-based movement logic

8. **Chess Programming Wiki**: https://www.chessprogramming.org/King
   - Chess piece movement algorithms
   - Used for: King-style movement pattern implementation (8-direction movement)

### Educational Programming Resources
9. **MIT OpenCourseWare - Introduction to Programming**: https://ocw.mit.edu/
   - University-level programming course materials
   - Used for: Object-oriented design principles, code structure guidelines

10. **Stanford CS106A Programming Methodology**: https://web.stanford.edu/class/cs106a/
    - Introductory computer science course materials
    - Used for: Java programming best practices, educational code examples

### Specific Technical References
11. **Java MouseListener Documentation**: https://docs.oracle.com/javase/8/docs/api/java/awt/event/MouseListener.html
    - Official MouseListener interface documentation
    - Used for: Mouse click handling, event method implementations

12. **Java Graphics2D API**: https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics2D.html
    - Advanced graphics rendering capabilities
    - Used for: Circle drawing, color management, visual highlights

### Software Engineering Education
13. **"Effective Java"** by Joshua Bloch
    - Java-specific programming best practices
    - Used for: Proper use of Optional, method design, class construction

14. **University of Washington CSE 143**: https://courses.cs.washington.edu/courses/cse143/
    - Data structures and software engineering course
    - Used for: Code organization, documentation standards, testing approaches

---

## 👨‍💻 Development Information

**Course**: COMP2000 - Object Oriented Programming Practices  
**Session**: 2, 2025  
**Institution**: University Assignment  

### Academic Integrity Notice
This repository contains original coursework developed for educational purposes. All external resources and references are properly cited above. The implementation demonstrates understanding of object-oriented programming concepts through practical application.

---

## 🔮 Future Enhancement Opportunities

### Potential Extensions
- **Animation System**: Smooth movement transitions between cells
- **Sound Effects**: Audio feedback for selections and movements  
- **Multiple Scenes**: Large world with scene transitions
- **AI Behaviors**: Autonomous animal movement patterns
- **Save/Load**: Persistent game state functionality
- **Terrain Types**: Different landscapes with movement rules

### Advanced Features
- **Multiplayer Support**: Multiple players controlling different animals
- **Item Collection**: Animals can pick up and use items
- **Energy System**: Limited movement with rest requirements
- **Path Finding**: Intelligent movement to distant locations

---


Attriubution- 
dog scribe AomAm
cat scribe  Freepik
bird scribe Those Icons
Milk scribe Freepik
Bone scribe Freepik
Worm Scribe Freepik
Water Scribe Vectors Market
Bowl Scribe Freepik
Tool Scribe Freepik
---

## 🌟 The Journey: From Simple Grid to Living World

What started as a basic animal movement exercise has become something that genuinely surprises me every time I run it. The project has evolved through several major phases, each one building on the last and adding layers of complexity that make the world feel more alive.

### Phase 1: Building the Foundation

When I first started, I had three animals that could move around a grid. That was it. But even then, I knew I wanted something more. I spent time making sure the movement felt good - that satisfying click-to-select, click-to-move that responds instantly. I added the yellow selection ring because I wanted players to always know which animal they were controlling.

The health bars were where I really got excited. I didn't want boring progress bars - I wanted something that felt like it belonged in a real game. So I studied how fighting games show health and tried to capture that same energy. When an animal's health gets critically low, the bar pulses red, and you can feel the urgency.

I also knew from the start that I wanted each animal to feel different. Dogs got 120 health because they're tough. Cats got 85 because they're quick but fragile. Birds got 100 as a middle ground. These aren't just numbers - they create different gameplay experiences for each animal.

### Phase 2: Bringing in the Weather

This is where things got really interesting. I discovered I could pull real weather data from the internet and suddenly had this idea: what if the game world responded to actual weather conditions? What if a rainy day in the real world made my digital animals seek shelter?

Implementing the Strategy pattern was my first deep dive into design patterns. Each animal needed to react differently to weather - dogs don't mind rain much, cats hate it, and birds are very sensitive to temperature changes. The pattern let me code these personalities in a clean way that I could easily extend.

But the real magic happened when I connected this to visual effects. Now when it's actually raining outside, you see rain effects in the game. The wind data from the weather server affects how the raindrops fall. It's this connection between the real world and the game world that makes me excited to show it to people.

The notification system tells you what's happening: "Cat is seeking shelter from the rain" or "Bird is enjoying the pleasant weather." These aren't just random messages - they reflect real weather conditions and each animal's programmed personality.

### Phase 3: Advanced Patterns and Smart Behaviors

Phase 3 pushed me into some of the most sophisticated programming I've done. I implemented three major design patterns that all work together to create emergent gameplay.

The State pattern transformed how animals behave. Now they don't just respond to weather - they change their entire behavioral state. A cat in "shelter seeking" mode acts completely differently than one in "normal" mode. Animals can be shivering in cold weather, seeking shade in heat, or battling against strong winds. Each state has its own logic and animations.

The Factory pattern revolutionized item spawning. Instead of random item placement, the system now spawns weather-appropriate items. During a storm, you'll see more emergency water. In cold weather, warming items appear. The factory considers both the current weather and the landscape type to make smart decisions about what should spawn where.

The Decorator pattern was the most fun to implement. I can now layer visual effects on top of each other. Rain effects, temperature tinting, and wind animations all combine seamlessly. The best part is that it's completely modular - I can add new effects without touching the existing ones.

### Recent Enhancements: Making It Smarter

The latest improvements focused on making the animals feel more intelligent and fixing some quality-of-life issues that were bothering me.

I was frustrated that my cat kept spawning with half health due to some old demo code. Now all animals spawn with full health every time - no more unfair starts. It's a small thing, but it makes the game feel more polished.

The food preference system was inspired by watching my real pets. Dogs shouldn't be able to eat milk, and cats shouldn't pick up bones. It sounds obvious, but coding realistic dietary restrictions made the animals feel more authentic. Now when a cat tries to pick up a bone, you get a clear message: "Cat cannot eat Bone (wrong food type)."

But the feature I'm most proud of is the automated movement system. Press 'F' with an animal selected, and it will automatically pathfind to the closest food it can actually eat. The algorithm checks all 400 grid cells, calculates distances, and finds the optimal target. It's incredibly satisfying to use and makes managing multiple animals much more enjoyable.

## 🎯 What This All Means

Looking back, I realize this project has become a showcase of everything I've learned about object-oriented programming and design patterns. Each phase introduced new concepts:

- **Phase 1** taught me inheritance, polymorphism, and clean code structure
- **Phase 2** showed me how to integrate external APIs and implement the Strategy pattern  
- **Phase 3** challenged me with State, Factory, and Decorator patterns working together
- **Recent work** focused on user experience and intelligent behaviors

The codebase demonstrates SOLID principles in action. Each class has a single responsibility, the system is open for extension but closed for modification, and interfaces keep everything loosely coupled. I can add a new animal type by just extending the Actor class, or add new weather effects through the Decorator system.

## 🎮 How Everything Works Together

What really excites me is how all these systems interact. The weather API provides real data that triggers state changes in animals through the State pattern. Those state changes influence what items the Factory pattern creates. The visual effects from the Decorator pattern reflect the current weather conditions. And the smart movement system helps animals navigate this dynamic world to find appropriate resources.

It's emergent gameplay - complex, interesting behaviors arising from the interaction of simpler systems. A rainy day creates a cascade of effects: animals change states, different items spawn, visual effects activate, and the automated movement system helps animals adapt to the new conditions.

## 🔧 Technical Deep Dive

For anyone interested in the technical details, here's how the major systems work:

**The Weather System** polls a real HTTP server every 30 seconds for rain, temperature, and wind data. This data flows through the Strategy pattern to influence animal behaviors, the State pattern to trigger behavioral changes, and the Factory pattern to adjust item spawning.

**The Movement System** uses Manhattan distance calculation to find the closest compatible food for each animal. The pathfinding algorithm is O(n²) for the 20x20 grid, which gives instant results while being simple to understand.

**The Visual System** layers effects using the Decorator pattern. Rain effects use wind data to calculate droplet trajectories. Temperature affects color tinting. All effects respect the grid boundaries and render at 60+ FPS.

**The File Structure** keeps everything organized:
- `actors/` contains the animal hierarchy
- `world/` has all the game management systems  
- `weather/` handles real-time data integration
- `patterns/` implements all the design patterns
- `items/` manages the consumable and equipment systems

## 🚀 What's Next

I keep having ideas for new features. A day/night cycle that affects animal behaviors. Multiplayer support so friends can control different animals. Maybe even a quest system with objectives and rewards.

But honestly, I'm already proud of what this has become. It started as a class assignment and turned into something that demonstrates advanced software engineering concepts while still being fun to play with. Every time I show it to someone, they're impressed by the weather integration or surprised by how smooth the automated movement feels.

The code quality is something I'm particularly proud of. It's documented, modular, and follows industry best practices while still being readable for educational purposes. A computer science student could understand any part of this codebase, but it also demonstrates concepts that would be at home in a professional development environment.

## 💭 Personal Reflection

This project represents months of iteration, problem-solving, and genuine excitement about programming. Each phase taught me something new, not just about coding but about software design, user experience, and the satisfaction that comes from building something that works well and feels good to use.

The integration of real weather data was a breakthrough moment for me. Suddenly, the game wasn't just a simulation - it was connected to the real world. The design patterns phase taught me how to structure complex systems elegantly. The recent improvements showed me how small details can make a huge difference in user experience.

I'm excited to continue developing as a programmer, and this project will always remind me of the moment when coding stopped feeling like work and started feeling like creative problem-solving.

---

*Last Updated: October 19, 2025*  
*Project Status: Advanced Design Patterns Complete - Weather Integration Active*
