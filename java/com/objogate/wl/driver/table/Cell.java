/**
 * Identifies a cell in a Table based on row and column
 */
package com.objogate.wl.driver.table;

import javax.swing.JTable;

public class Cell implements Location {
    public final int row;
    public final int col;

    public Cell(int row, int col) {
      this.row = row;
      this.col = col;
    }
    public Object valueFrom(JTable table) { return table.getValueAt(row, col); }
    public Cell asCellIn(JTable unused) { return this; }

    @Override public String toString() {
        return "r" + row + " x " + "c" + col;
    }
}