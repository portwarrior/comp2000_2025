package world;

import actors.Actor;
import items.Item;
import items.VisualItem;
import java.awt.Point;
import java.util.Optional;

/**
 * handles clicking on animals and moving them,handles item pickup when animals move
 */
public class AnimalMover {
    
    private AnimalSelection selection;
    private ItemManager itemManager;
    
    public AnimalMover() {
        this.selection = new AnimalSelection();
    }
    
    /**
     * Set the item manager for pickup interactions
     */
    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }
    
    /**
     * Handle a mouse click 
     */
    public void handleClick(Point clickPoint, Cell[][] gridCells, Actor[] animals, Cell[] animalCells) {
        // Find which cell was clicked
        Optional<Cell> clickedCellOpt = findCellAtPoint(clickPoint, gridCells);
        
        if (!clickedCellOpt.isPresent()) {
            return; // clicked outside grid
        }
        
        Cell clickedCell = clickedCellOpt.get();
        
        // If we have a selection and clicked on a possible move
        if (selection.hasSelection() && selection.isPossibleMove(clickedCell)) {
            moveAnimalToCell(clickedCell, animals, animalCells);
            selection.clearSelection();
            return;
        }
        
        // Check if we clicked on an animal
        Actor clickedActor = findActorAtCell(clickedCell, animals, animalCells);
        
        if (clickedActor != null) {
            // Select animal
            selection.selectActor(clickedActor, clickedCell, gridCells);
        } else {
            // clicked empty space, clear selection
            selection.clearSelection();
        }
    }
    
    /**
     * move the selected animal to a new cell and handle item pickup
     */
    private void moveAnimalToCell(Cell targetCell, Actor[] animals, Cell[] animalCells) {
        Actor selectedActor = selection.getSelectedActor();
        
        // Find the actor in the arrays and update its position
        for (int i = 0; i < animals.length; i++) {
            if (animals[i] == selectedActor) {
                animalCells[i] = targetCell;
                // Update the actor's visual position
                selectedActor.moveTo(targetCell);
                
                // Check for item pickup
                if (itemManager != null) {
                    VisualItem itemAtCell = itemManager.getItemAt(targetCell);
                    if (itemAtCell != null) {
                        // Try to pick up item
                        Item item = itemManager.removeItemAt(targetCell);
                        if (item != null) {
                            boolean pickedUp = selectedActor.getInventory().addItem(item);
                            if (pickedUp) {
                                System.out.println(selectedActor.getClass().getSimpleName() + 
                                                 " picked up " + item.getName());
                            } else {
                                // Inventory full, drop item 
                                itemManager.spawnItem(item.getName().toLowerCase(), targetCell);
                                System.out.println(selectedActor.getClass().getSimpleName() + 
                                                 "'s inventory is full!");
                            }
                        }
                    }
                }
                break;
            }
        }
    }
    
    /**
     * finds cell contains given point
     */
    private Optional<Cell> findCellAtPoint(Point point, Cell[][] gridCells) {
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 20; row++) {
                Cell cell = gridCells[col][row];
                if (point.x >= cell.x && point.x < cell.x + Cell.SIZE &&
                    point.y >= cell.y && point.y < cell.y + Cell.SIZE) {
                    return Optional.of(cell);
                }
            }
        }
        return Optional.empty();
    }
    
    /**
     * finds which actor is at given cell
     */
    private Actor findActorAtCell(Cell cell, Actor[] animals, Cell[] animalCells) {
        for (int i = 0; i < animals.length; i++) {
            if (animalCells[i] == cell) {
                return animals[i];
            }
        }
        return null;
    }
    
    public AnimalSelection getSelection() {
        return selection;
    }
}