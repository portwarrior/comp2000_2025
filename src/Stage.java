import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import actors.Actor;
import actors.Bird;
import actors.Cat;
import actors.Dog;
import world.AnimalMover;
import world.Cell;
import world.MovementHighlights;
import world.GameWorldManager;
import world.HealthBarRenderer;

public class Stage implements MouseListener, KeyListener {
  Grid grid;
  List<Actor> actors;
  List<Cell> animalCells;
  AnimalMover mover;
  GameWorldManager worldManager;
  private long lastUpdateTime;

  public Stage() {
    grid = new Grid();
    actors = new ArrayList<>();
    animalCells = new ArrayList<>();
    mover = new AnimalMover();
    lastUpdateTime = System.currentTimeMillis();
    
    // Initialize the world manager with the grid
    worldManager = new GameWorldManager(grid.cells, System.currentTimeMillis());
    
    // Set up item manager for animal movement
    mover.setItemManager(worldManager.getItemManager());
    
    // Create animals and track their cells
    Optional<Cell> catCellOpt = grid.cellAtColRow(0, 0);
    Optional<Cell> dogCellOpt = grid.cellAtColRow(0, 15);
    Optional<Cell> birdCellOpt = grid.cellAtColRow(12, 9);
    
    if (catCellOpt.isPresent() && dogCellOpt.isPresent() && birdCellOpt.isPresent()) {
      Cell catCell = catCellOpt.get();
      Cell dogCell = dogCellOpt.get();
      Cell birdCell = birdCellOpt.get();
      
      Cat cat = new Cat(catCell);
      Dog dog = new Dog(dogCell);
      Bird bird = new Bird(birdCell);
      
      actors.add(cat);
      actors.add(dog);
      actors.add(bird);
      
      // Add actors to world manager
      worldManager.addActor(cat);
      worldManager.addActor(dog);
      worldManager.addActor(bird);
      
      animalCells.add(catCell);
      animalCells.add(dogCell);
      animalCells.add(birdCell);
    }
  }

  public void paint(Graphics g, Point mouseLoc) {
    // Update world manager periodically
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastUpdateTime > 2000) { // Every 2 seconds
      worldManager.updateWorld();
      lastUpdateTime = currentTime;
    }
    
    // First render the world backgrounds (zones)
    worldManager.render(g);
    
    // Then render the grid lines and cell borders
    grid.paint(g, mouseLoc);
    
    // Then render actors on top
    for(Actor a: actors) {
      a.paint(g);
    }
    
    // Draw movement highlights (grey circles and selection ring)
    MovementHighlights.drawHighlights(g, mover.getSelection());
    
    // Render weather effects on top of everything
    Graphics2D weatherG2d = (Graphics2D) g;
    worldManager.renderWeatherOverlay(weatherG2d);
    
    Optional<Cell> underMouse = grid.cellAtPoint(mouseLoc);
    if(underMouse.isPresent()) {
      Cell hoverCell = underMouse.get();
      g.setColor(Color.DARK_GRAY);
      g.drawString(String.valueOf(hoverCell.col) + String.valueOf(hoverCell.row), 740, 30);
      
      // Show zone info
      String zoneInfo = worldManager.getCellInfo(hoverCell);
      g.drawString(zoneInfo, 740, 50);
    }
    
    // Debug: Show animal inventories
    g.setColor(Color.BLACK);
    g.drawString("Animal Inventories:", 740, 80);
    
    int yOffset = 100;
    for (int i = 0; i < actors.size(); i++) {
      Actor actor = actors.get(i);
      String animalType = actor.getClass().getSimpleName();
      String inventoryInfo = actor.getInventory().getInventorySummary();
      
      g.drawString(animalType + ":", 740, yOffset);
      g.drawString(inventoryInfo, 740, yOffset + 15);
      yOffset += 40;
    }
    
    // Draw Tekken-style health bars below inventories
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    
    // Position health bars below inventory display with some spacing
    int healthBarStartY = yOffset + 20;
    HealthBarRenderer.drawHealthBars(g2d, actors, 740, healthBarStartY, 45);
    
    // Add instructions for new features
    g.setColor(Color.DARK_GRAY);
    int instructY = healthBarStartY + 160;
    g.drawString("Controls:", 740, instructY);
    g.drawString("- Click animal to select", 740, instructY + 20);
    g.drawString("- Click highlighted cell to move", 740, instructY + 35);
    g.drawString("- Press 'F' to move to closest food", 740, instructY + 50);
    g.drawString("Food rules:", 740, instructY + 75);
    g.drawString("- Dogs eat bones", 740, instructY + 90);
    g.drawString("- Cats drink milk", 740, instructY + 105);
    g.drawString("- Birds eat worms", 740, instructY + 120);
    g.drawString("- All animals drink water", 740, instructY + 135);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Point clickPoint = e.getPoint();
    
    // Convert lists to arrays for the mover
    Actor[] animalArray = actors.toArray(new Actor[0]);
    Cell[] cellArray = animalCells.toArray(new Cell[0]);
    
    mover.handleClick(clickPoint, grid.cells, animalArray, cellArray);
    
    // Update the animal cells list after movement
    for (int i = 0; i < animalArray.length; i++) {
      animalCells.set(i, cellArray[i]);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // Not needed for this implementation
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // Not needed for this implementation
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // Not needed for this implementation
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // Not needed for this implementation
  }
  
  // KeyListener implementation for "move to food" button
  @Override
  public void keyPressed(KeyEvent e) {
    // Press 'F' to move selected animal to closest food
    if (e.getKeyCode() == KeyEvent.VK_F) {
      Actor[] animalArray = actors.toArray(new Actor[0]);
      Cell[] cellArray = animalCells.toArray(new Cell[0]);
      
      mover.moveToClosestFood(grid.cells, animalArray, cellArray);
      
      // Update the animal cells list after movement
      for (int i = 0; i < animalArray.length; i++) {
        animalCells.set(i, cellArray[i]);
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Not needed for this implementation
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Not needed for this implementation
  }
}
