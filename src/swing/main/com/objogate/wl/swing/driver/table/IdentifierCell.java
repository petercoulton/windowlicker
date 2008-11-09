/**
 * Identifies a Cell in a Table based on row and column identifier
 */
package com.objogate.wl.swing.driver.table;

import javax.swing.JTable;

public class IdentifierCell implements Location {
  private final int row;
  public final Object columnIdentifier;

  public IdentifierCell(int row, Object columnIdentifier) {
    this.row = row;
    this.columnIdentifier = columnIdentifier;
  }

  public Cell asCellIn(JTable table) {
    return new Cell(row, viewIndex(table));
  }
  @Override public String toString() {
      return "row " + row + " at " + columnIdentifier;
  }
  private int viewIndex(JTable table) {
    int modelIndex = table.getColumn(columnIdentifier).getModelIndex();
    return table.convertColumnIndexToView(modelIndex);
  }
}