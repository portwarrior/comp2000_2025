package actors;
import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

import world.Cell;


public class Bird extends Actor {
  public Bird(Cell inLoc) {
    loc = inLoc;
    color = Color.GREEN;
    setHealth(100); // Birds have default health
    // Always spawn with full health
    currentHealth = maxHealth;
    buildDisplay();
  }
  
  @Override
  protected void buildDisplay() {
    display = new ArrayList<Polygon>();
    Polygon wing1 = new Polygon();
    wing1.addPoint(loc.x + 5, loc.y + 5);
    wing1.addPoint(loc.x + 15, loc.y + 17);
    wing1.addPoint(loc.x + 5, loc.y + 17);
    Polygon wing2 = new Polygon();
    wing2.addPoint(loc.x + 30, loc.y + 5);
    wing2.addPoint(loc.x + 20, loc.y + 17);
    wing2.addPoint(loc.x + 30, loc.y + 17);
    Polygon body = new Polygon();
    body.addPoint(loc.x + 15, loc.y + 10);
    body.addPoint(loc.x + 20, loc.y + 10);
    body.addPoint(loc.x + 20, loc.y + 25);
    body.addPoint(loc.x + 15, loc.y + 25);
    display.add(body);
    display.add(wing1);
    display.add(wing2);
  }
}
