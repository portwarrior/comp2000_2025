package actors;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

import world.Cell;
import core.AnimalInventory;

public abstract class Actor {
  Color color;
  Cell loc;
  List<Polygon> display;
  AnimalInventory inventory;

  public Actor() {
    // Default inventory size of 10 units
    this.inventory = new AnimalInventory(10);
  }

  public void paint(Graphics g) {
    for(Polygon p: display) {
      g.setColor(color);
      g.fillPolygon(p);
      g.setColor(Color.GRAY);
      g.drawPolygon(p);
    }
  }
  
  /**
   * Move this actor to a new cell and update its visual position
   */
  public void moveTo(Cell newCell) {
    this.loc = newCell;
    // Rebuild the display polygons at the new location
    buildDisplay();
  }
  
  /**
   * Each actor type needs to implement how to build its visual display
   */
  protected abstract void buildDisplay();
  
  /**
   * Get the current location of this actor
   */
  public Cell getLocation() {
    return loc;
  }
  
  /**
   * Get this actor's inventory
   */
  public AnimalInventory getInventory() {
    return inventory;
  }
}
