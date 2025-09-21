package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Cell extends Rectangle {
  public static final int SIZE = 35;  
  public char col;  
  public int row;   

  public Cell(char inCol, int inRow, int x, int y) {
    super(x, y, SIZE, SIZE);
    col = inCol;
    row = inRow;
  }

  public void paint(Graphics g, Point mousePos) {

    if(contains(mousePos)) {
      g.setColor(new Color(0, 0, 0, 50)); 
      g.fillRect(x, y, SIZE, SIZE);
    }
    g.setColor(Color.BLACK);
    g.drawRect(x, y, SIZE, SIZE);
  }

  public boolean contains(Point p) {
    if(p != null) {
      return super.contains(p);
    } else {
      return false;
    }
  }
}
