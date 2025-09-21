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

To get started- Run powershell file with `./make.ps1 clean` then run the code in main file.

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

### Software Engineering Practices
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed Principle**: Easy to add new animals without modifying existing code
- **Clean Code**: Readable, well-commented, educational-level implementation
- **Separation of Concerns**: UI, logic, and data layers clearly separated

---

## 📚 Academic References & Citations

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
*Last Updated: September 20, 2025*  
*Project Status: Fully Functional - Movement System Complete*
