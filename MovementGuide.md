# Animal Movement System - Simple Guide

## What it does:
Animals can move like a king in chess - to any of the 8 surrounding cells.

## How to use it:

### 1. Basic Components Created:
- `AnimalSelection.java` - Tracks which animal is selected and where it can move
- `AnimalMover.java` - Handles mouse clicks for selecting and moving animals  
- `MovementHighlights.java` - Draws grey circles and selection rings
- Modified `Actor.java` - Added moveTo() method for repositioning animals

### 2. Visual Feedback:
- **Yellow ring** around selected animal
- **Grey circles** on cells where the animal can move
- **8 possible moves** in all directions (like chess king)

### 3. How the movement works:

```
[↖] [↑] [↗]
[←] [🐕] [→] 
[↙] [↓] [↘]
```

### 4. Mouse Controls:
- **Click on an animal** → Select it (shows yellow ring + grey circles)
- **Click on a grey circle** → Move the animal there
- **Click on empty space** → Clear selection
- **Click on different animal** → Select that animal instead

### 5. Integration with your Stage class:

You need to add these three things to your Stage class:

#### In Stage constructor:
```java
private AnimalMover mover = new AnimalMover();
```

#### In Stage.paint() method (after drawing grid and animals):
```java
// Draw movement highlights
MovementHighlights.drawHighlights(g, mover.getSelection());
```

#### In Stage mouse click handler:
```java
public void mouseClicked(MouseEvent e) {
    Point clickPoint = e.getPoint();
    mover.handleClick(clickPoint, grid.cells, animals, animalCells);
    repaint(); // Refresh the display
}
```

### 6. Data you need to track:
- `Actor[] animals` - Array of your animals
- `Cell[] animalCells` - Which cell each animal is on
- `Grid grid` - Your existing grid with cells

### 7. Simple Example Usage:
```java
// Create the movement system
AnimalMover mover = new AnimalMover();

// In your paint method
MovementHighlights.drawHighlights(g, mover.getSelection());

// In your mouse handler
mover.handleClick(mousePoint, grid.cells, animals, animalCells);
```

## That's it! 
Your animals will now move like chess kings with visual feedback.