package com.objogate.wl.driver;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.driver.JTableDriver.Cell;
import com.objogate.wl.driver.JTableDriver.IdentifierCell;
import com.objogate.wl.driver.JTableDriver.Location;

public class JTableCellManipulation implements ComponentManipulation<JTable> {
    private final Location location;

    private Component renderedCell;
    private Component editorComponent;

    public JTableCellManipulation(int row, int col) {
      this.location = new Cell(row, col);
    }

    public JTableCellManipulation(int row, Object columnIdentifier) {
      this.location = new IdentifierCell(row, columnIdentifier);
    }

    public void manipulate(JTable table) {
      final Cell cell = location.asCellIn(table);
      this.renderedCell = render(table, cell);
      this.editorComponent = editor(table, cell);
    }

    private Component editor(JTable table, Cell cell) {
        TableCellEditor tableCellEditor = table.getCellEditor(cell.row, cell.col);
        return tableCellEditor.getTableCellEditorComponent(table, 
                  cell.valueFrom(table), 
                  false, cell.row, cell.col);
    }

    public Component getRenderedCell() {
        return renderedCell;
    }

    public Component getEditorComponent() {
        return editorComponent;
    }

    public static Component render(JTable table, int row, Object columIdentifier) {
      return render(table, new IdentifierCell(row, columIdentifier).asCellIn(table));
    }

    public static Component render(JTable table, int row, int col) {
      return render(table, new Cell(row, col));
    }
    
    public static Component render(JTable table, Location location) { 
        Cell cell = location.asCellIn(table);
        TableCellRenderer cellRenderer = table.getCellRenderer(cell.row, cell.col);
        boolean isSelected = JTableDriver.arrayContains(table.getSelectedRows(), cell.row);
        boolean hasFocus = isSelected && JTableDriver.arrayContains(table.getSelectedColumns(), cell.col);
        return cellRenderer.getTableCellRendererComponent(table, 
                              cell.valueFrom(table), 
                              isSelected, hasFocus, cell.row, cell.col);
    }
}
