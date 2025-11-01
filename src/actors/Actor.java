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
  
  // Health system
  protected int maxHealth;
  protected int currentHealth;

  public Actor() {
    // Default inventory size of 10 units
    this.inventory = new AnimalInventory(10);
    
    // Default health values (can be overridden by subclasses)
    this.maxHealth = 100;
    this.currentHealth = maxHealth;
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
  
  /**
   * Get current health
   */
  public int getCurrentHealth() {
    return currentHealth;
  }
  
  /**
   * Get maximum health
   */
  public int getMaxHealth() {
    return maxHealth;
  }
  
  /**
   * Get health as a percentage (0.0 to 1.0)
   */
  public double getHealthPercentage() {
    return (double) currentHealth / maxHealth;
  }
  
  /**
   * Take damage and reduce health
   */
  public void takeDamage(int damage) {
    currentHealth = Math.max(0, currentHealth - damage);
  }
  
  /**
   * Heal and increase health
   */
  public void heal(int healAmount) {
    currentHealth = Math.min(maxHealth, currentHealth + healAmount);
  }
  
  /**
   * Simple eating mechanism - animals gain health from preferred foods
   * Dogs like bones, Cats like milk, Birds like worms
   */
  public void eatFood(String foodType) {
    int healthGain = 0;
    
    // Simple food preferences for each animal type
    String animalType = this.getClass().getSimpleName();
    
    if ("Dog".equals(animalType) && foodType.toLowerCase().contains("bone")) {
      healthGain = 20; // Dogs gain 20% health from bones
    } else if ("Cat".equals(animalType) && foodType.toLowerCase().contains("milk")) {
      healthGain = 15; // Cats gain 15% health from milk
    } else if ("Bird".equals(animalType) && foodType.toLowerCase().contains("worm")) {
      healthGain = 25; // Birds gain 25% health from worms
    } else if (foodType.toLowerCase().contains("water")) {
      healthGain = 5;  // All animals gain 5% health from water
    }
    
    if (healthGain > 0) {
      heal(healthGain);
      if ("true".equals(System.getProperty("DEBUG_MODE"))) {
        System.out.println(animalType + " ate " + foodType + " and gained " + healthGain + " health! Current health: " + currentHealth + "/" + maxHealth);
      }
    }
  }
  
  /**
   * Check if the actor is alive
   */
  public boolean isAlive() {
    return currentHealth > 0;
  }
  
  /**
   * Set health values (for different animal types)
   */
  protected void setHealth(int maxHealth) {
    this.maxHealth = maxHealth;
    this.currentHealth = maxHealth;
  }
  
  /**
   * Check if this animal can eat the given food type
   * Students can easily understand food preferences:
   * - Dogs eat bones (and water)
   * - Cats eat milk (and water) 
   * - Birds eat worms (and water)
   */
  public boolean canEat(String foodType) {
    String animalType = this.getClass().getSimpleName();
    String food = foodType.toLowerCase();
    
    // All animals can drink water
    if (food.contains("water")) {
      return true;
    }
    
    // Animal-specific food preferences
    if ("Dog".equals(animalType) && food.contains("bone")) {
      return true; // Dogs eat bones
    } else if ("Cat".equals(animalType) && food.contains("milk")) {
      return true; // Cats drink milk
    } else if ("Bird".equals(animalType) && food.contains("worm")) {
      return true; // Birds eat worms
    }
    
    return false; // nope, can't eat that
  }
  
  /**
   * Get the preferred food type for this animal
   */
  public String getPreferredFood() {
    String animalType = this.getClass().getSimpleName();
    
    if ("Dog".equals(animalType)) {
      return "Bone";
    } else if ("Cat".equals(animalType)) {
      return "Milk";
    } else if ("Bird".equals(animalType)) {
      return "Worm";
    }
    
    return "Water"; // fallback - everyone needs water
  }
}
