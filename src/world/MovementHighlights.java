package world;

import java.awt.Color;
import java.awt.Graphics;

/**
 * movement highlights 
 */
public class MovementHighlights {
    
    private MovementHighlights() {
        // Utility class
    }
    
    /**
     * Draw grey circles on cells where the selected animal can move
     */
    public static void drawHighlights(Graphics g, AnimalSelection selection) {
        if (!selection.hasSelection()) {
            return;
        }
        g.setColor(new Color(128, 128, 128, 180));
        // Draw a circle on each possible move
        for (Cell moveCell : selection.getPossibleMoves()) {
            drawGreyCircle(g, moveCell);
        }
        drawSelectionRing(g, selection.getSelectedCell());
    }
    
    /**
     * Draw a grey circle in center of cell
     */
    private static void drawGreyCircle(Graphics g, Cell cell) {
        int cellSize = Cell.SIZE;
        int circleRadius = cellSize / 4;
        int centerX = cell.x + cellSize / 2;
        int centerY = cell.y + cellSize / 2;
        g.fillOval(centerX - circleRadius, centerY - circleRadius, 
                   circleRadius * 2, circleRadius * 2);
    }
    

    private static void drawSelectionRing(Graphics g, Cell cell) {
        g.setColor(Color.YELLOW);
        int cellSize = Cell.SIZE;
        int ringThickness = 3;
        for (int i = 0; i < ringThickness; i++) {
            g.drawRect(cell.x + i, cell.y + i, 
                       cellSize - 2 * i, cellSize - 2 * i);
        }
    }
}