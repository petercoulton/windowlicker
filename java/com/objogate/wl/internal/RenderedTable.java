package com.objogate.wl.internal;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Maps a table to the rendered cells visible in that table.
 *
 * This is not inherently thread-safe.  The test that uses this object is responsible for
 * ensuring thread safety.  Basically, all calls to this object must be made on the Swing
 * thread. 
 */
public class RenderedTable {
    private final JTable table;
    public static final int HEADER = -1;

    public RenderedTable(JTable table) {
        this.table = table;
    }

    public String getTextFromCell(int rowIndex, Object propertyId) {
        return renderCell(table, rowIndex, indexOf(propertyId)).getText();
    }

    public int getJustificationOfCell(int rowIndex, Object propertyId) {
        return renderCell(table, rowIndex, indexOf(propertyId)).getHorizontalAlignment();
    }

    private JLabel renderCell(JTable table, int rowIndex, int columnIndex) {
        if (rowIndex == HEADER) {
            return renderHeaderCell(table, rowIndex, columnIndex);
        }
        else {
            return renderTableCell(table, rowIndex, columnIndex);
        }
    }
    
    private JLabel renderTableCell(JTable table, int rowIndex, int columnIndex) {
        TableCellRenderer cellRenderer = table.getCellRenderer(rowIndex, columnIndex);
        Object cellValue = table.getValueAt(rowIndex, columnIndex);
        ListSelectionModel selectionModel = table.getSelectionModel();
        boolean selected = rowIndex >= selectionModel.getMinSelectionIndex() && rowIndex <= selectionModel.getMaxSelectionIndex();
        return (JLabel) cellRenderer.getTableCellRendererComponent(table, cellValue, selected, false, rowIndex, columnIndex);
    }

    private JLabel renderHeaderCell(JTable table, int rowIndex, int columnIndex) {
        TableCellRenderer renderer = table.getTableHeader().getDefaultRenderer();
        int modelIndex = table.convertColumnIndexToModel(columnIndex);
        TableColumn tableColumn = table.getColumnModel().getColumn(modelIndex);
        Object cellValue = tableColumn.getHeaderValue();
        return (JLabel) renderer.getTableCellRendererComponent(table, cellValue, false, false, rowIndex, columnIndex);
    }

    public Color getBackgroundColour(int rowIndex, int columnIndex) {
        return renderCell(table, rowIndex, columnIndex).getBackground();
    }

    public Color getForegroundColour(int rowIndex, int columnIndex) {
        return renderCell(table, rowIndex, columnIndex).getForeground();
    }

    public Font getFont(int rowIndex, int columnIndex) {
        return renderCell(table, rowIndex, columnIndex).getFont();
    }

    public void selectRow(int rowIndex) {
        table.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
    }

    public int indexOf(Object columnIdentifier) {
        return table.getColumnModel().getColumnIndex(columnIdentifier);
    }

    public void moveColumnTo(Object propertyId, int toIndex) {
        table.getColumnModel().moveColumn(indexOf(propertyId), toIndex);
    }

    public void moveColumnToEnd(Object column) {
        moveColumnTo(column, table.getColumnCount() - 1);
    }
}
