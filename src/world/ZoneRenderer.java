package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Renders landscape backgrounds
 */
public class ZoneRenderer {
    
    private Map<Cell, Zone> cellToZone;
    private Map<Zone, List<Gate>> wallsAndGates;
    
    public ZoneRenderer() {
        this.cellToZone = new HashMap<>();
        this.wallsAndGates = new HashMap<>();
    }
    
    /**
     * Set up the renderer with zone information
     */
    public void initialize(List<Zone> zones) {
        cellToZone.clear();
        wallsAndGates.clear();
        // Build lookup maps
        for (Zone zone : zones) {
            for (Cell cell : zone.getCells()) {
                cellToZone.put(cell, zone);
            }
            wallsAndGates.put(zone, zone.getGates());
        }
    }
    
    /**
     * landscape backgrounds 
     */
    public void drawLandscapeBackgrounds(Graphics g, Cell[][] gridCells) {
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 20; row++) {
                Cell cell = gridCells[col][row];
                Zone zone = cellToZone.get(cell);
                if (zone != null) {
                    drawCellBackground(g, cell, zone.getLandscape());
                }
            }
        }
    }
    
    
    public void drawWallsAndGates(Graphics g, Cell[][] gridCells) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3)); // Thick walls
        
        // Draw walls
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 20; row++) {
                Cell cell = gridCells[col][row];
                Zone cellZone = cellToZone.get(cell);
                if (cellZone != null) {
                    drawWallsAroundCell(g2d, cell, cellZone, gridCells, col, row);
                }
            }
        }
                // Draw gates 
        drawAllGates(g2d);
    }
    
    /**
     *  background texture 
     */
    private void drawCellBackground(Graphics g, Cell cell, LandscapeType landscape) {
        int x = cell.x;
        int y = cell.y;
        int size = Cell.SIZE;
        
        switch (landscape) {
            case NEUTRAL:
                drawNeutralBackground(g, x, y, size);
                break;
                
            case LEGOS:
                drawLegosBackground(g, x, y, size);
                break;
                
            case SAND_DUNES:
                drawSandDunesBackground(g, x, y, size);
                break;
                
            case DEPTHS:
                drawDepthsBackground(g, x, y, size);
                break;
        }
    }
    
    /**
     * Draw NEUTRAL background - clean white/light gray
     */
    private void drawNeutralBackground(Graphics g, int x, int y, int size) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, size, size);
        g.setColor(new Color(240, 240, 240)); // Light gray
        g.drawRect(x, y, size-1, size-1);
        g.setColor(new Color(250, 250, 250)); // Very light gray
        int mid = size / 2;
        g.drawLine(x, y + mid, x + size, y + mid); 
        g.drawLine(x + mid, y, x + mid, y + size);
    }
    
    /**
     *  LEGOS background
     */
    private void drawLegosBackground(Graphics g, int x, int y, int size) {
        // Base color - bright LEGO-like colors
        g.setColor(new Color(50, 250, 55));//LIMEGREEN
        g.fillRect(x, y, size, size);
        //  LEGO studs
        g.setColor(Color.WHITE);
        int studSize = size / 6;
        int spacing = size / 3;
        
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int studX = x + spacing/2 + i * spacing;
                int studY = y + spacing/2 + j * spacing;
                g.fillOval(studX, studY, studSize, studSize);
            }
        }
        
        // Outline the cell
        g.setColor(new Color(0, 70, 200));
        g.drawRect(x, y, size-1, size-1);
    }
    
    /**
     *  SAND_DUNES background
     */
    private void drawSandDunesBackground(Graphics g, int x, int y, int size) {
        // Base sand color
        g.setColor(new Color(255, 180, 80)); // Bright orange sand
        g.fillRect(x, y, size, size);
        g.setColor(new Color(220, 160, 60)); // Darker sand
        
        //  wave pattern 
        for (int i = 0; i < 3; i++) {
            int waveY = y + (i + 1) * size / 4;
            
            // wavy line
            for (int px = x; px < x + size; px += 2) {
                int wave = (int) (Math.sin((px - x) * 0.3) * 3);
                g.fillRect(px, waveY + wave, 2, 2);
            }
        }

        // Texture dots
        g.setColor(new Color(200, 180, 120));
        for (int i = 0; i < 8; i++) {
            int dotX = x + (i * 7) % size;
            int dotY = y + (i * 11) % size;
            g.fillRect(dotX, dotY, 1, 1);
        }
    }
    
    /**
     * Draw DEPTHS background 
     */
    private void drawDepthsBackground(Graphics g, int x, int y, int size) {
        g.setColor(new Color(30, 80, 150)); // Deepblue
        g.fillRect(x, y, size, size);
        
        // Add water ripples
        g.setColor(new Color(50, 100, 170)); // lighter blue
        
        // Horizontal ripple lines
        for (int i = 0; i < 4; i++) {
            int rippleY = y + i * size / 4 + (size / 8);
            
            // Draw ripple line with variation
            for (int px = x; px < x + size; px += 3) {
                int ripple = (int) (Math.sin((px - x) * 0.4 + i) * 2);
                g.drawLine(px, rippleY + ripple, px + 1, rippleY + ripple);
            }
        }
        g.setColor(new Color(80, 130, 200)); //  blue
        for (int i = 0; i < 6; i++) {
            int highlightX = x + (i * 9) % size;
            int highlightY = y + (i * 7) % size;
            g.fillRect(highlightX, highlightY, 2, 1);
        }
    }
    
    private void drawWallsAroundCell(Graphics2D g2d, Cell cell, Zone cellZone, Cell[][] gridCells, int col, int row) {
        g2d.setColor(new Color(60, 60, 60)); // Dark gray walls
        
        int x = cell.x;
        int y = cell.y;
        int size = Cell.SIZE;
        if (row > 0) {
            Zone northZone = cellToZone.get(gridCells[col][row-1]);
            if (northZone != cellZone && !hasGateBetween(cell, gridCells[col][row-1])) {
                g2d.drawLine(x, y, x + size, y);
            }
        }

        if (row < 19) {
            Zone southZone = cellToZone.get(gridCells[col][row+1]);
            if (southZone != cellZone && !hasGateBetween(cell, gridCells[col][row+1])) {
                g2d.drawLine(x, y + size, x + size, y + size);
            }
        }
        if (col > 0) {
            Zone westZone = cellToZone.get(gridCells[col-1][row]);
            if (westZone != cellZone && !hasGateBetween(cell, gridCells[col-1][row])) {
                g2d.drawLine(x, y, x, y + size);
            }
        }
        if (col < 19) {
            Zone eastZone = cellToZone.get(gridCells[col+1][row]);
            if (eastZone != cellZone && !hasGateBetween(cell, gridCells[col+1][row])) {
                g2d.drawLine(x + size, y, x + size, y + size);
            }
        }
    }
    
    /**
     * Check if there's a gate between two cells
     */
    private boolean hasGateBetween(Cell cell1, Cell cell2) {
        Zone zone1 = cellToZone.get(cell1);
        Zone zone2 = cellToZone.get(cell2);
        
        if (zone1 == null || zone2 == null || zone1 == zone2) {
            return false;
        }
        
        // Check gates in both zones
        for (Gate gate : zone1.getGates()) {
            if (gate.connects(cell1, cell2)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Draw all gates as openings with special markers
     */
    private void drawAllGates(Graphics2D g2d) {
        g2d.setColor(new Color(100, 255, 100)); // Bright green for gates
        g2d.setStroke(new BasicStroke(2));
        
        for (Zone zone : wallsAndGates.keySet()) {
            for (Gate gate : zone.getGates()) {
                drawGateMarker(g2d, gate);
            }
        }
    }
    
    /**
     * Draw a visual marker for a gate (opening between zones)
     */
    private void drawGateMarker(Graphics2D g2d, Gate gate) {
        Cell fromCell = gate.getFromCell();
        Cell toCell = gate.getToCell();
        
        // Find the midpoint between the two cells
        int midX = (fromCell.x + toCell.x + Cell.SIZE) / 2;
        int midY = (fromCell.y + toCell.y + Cell.SIZE) / 2;
        
        // Draw a small gate symbol (circle)
        int gateSize = 8;
        g2d.fillOval(midX - gateSize/2, midY - gateSize/2, gateSize, gateSize);
        
        // Draw an arrow pointing through the gate
        g2d.setColor(new Color(50, 200, 50)); // Darker green
        g2d.drawLine(midX - 6, midY, midX + 6, midY);
        g2d.drawLine(midX + 3, midY - 3, midX + 6, midY);
        g2d.drawLine(midX + 3, midY + 3, midX + 6, midY);
    }
}