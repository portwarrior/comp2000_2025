🚀 How to Run

Normal Mode (Clean UI)
```powershell
./make.ps1
```

Debug Mode (Shows weather streaming in console)
```powershell
./make.ps1 -d
```

I used 15+ stream operations throughout the project, demonstrating filter, map, findFirst, average, anyMatch, and collect. I also show method references vs lambda expressions and proper Optional handling. See README 'Lambdas and Streams' section for detailed explanations.

Key Examples:
```java
// Filter + map + findFirst
weatherPoints.stream()
    .filter(p -> p.getGameX() == x)
    .mapToDouble(SimpleWeatherPoint::getIntensity)
    .findFirst()
    .orElse(0.0);

// Complex aggregation
animals.stream()
    .filter(animal -> pathfinder.hasPath(...))
    .mapToInt(animal -> spawnPoint.getDistanceTo(...))
    .average();
```

---
