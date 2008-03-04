package com.objogate.wl.driver;

import com.objogate.wl.ComponentManipulation;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class JTableCellManipulation implements ComponentManipulation<JTable> {
    private final int row;
    private Object columnIdentifier;
    private int col;

    private Component cell;
    private Component editorComponent;

    public JTableCellManipulation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public JTableCellManipulation(int row, Object columnIdentifier) {
        this.row = row;
        this.columnIdentifier = columnIdentifier;
    }

    public void manipulate(JTable table) {
        if(columnIdentifier != null) {
            cell = cell(table, row, columnIdentifier);
            editorComponent = editor(table, row, columnIdentifier);
        } else {
            cell = cell(table, row, col);
            editorComponent = editor(table, row, col);
        }
    }

    private Component editor(JTable table, int editorRow, Object editorColumnIdentifier) {
        int viewIndex = viewIndex(table, editorColumnIdentifier);
        return editor(table, editorRow, viewIndex);
    }

    private Component editor(JTable table, int editorRow, int editorCol) {
        TableCellEditor tableCellEditor = table.getCellEditor(editorRow, editorCol);
        return tableCellEditor.getTableCellEditorComponent(table, table.getValueAt(editorRow, editorCol), false, editorRow, editorCol);
    }

    public static Component cell(JTable table, int row, Object columIdentifier) {
        int viewIndex = viewIndex(table, columIdentifier);
        return cell(table, row, viewIndex);
    }

    private static int viewIndex(JTable table, Object columIdentifier) {
        int modelIndex = table.getColumn(columIdentifier).getModelIndex();
        return table.convertColumnIndexToView(modelIndex);
    }

    public static Component cell(JTable table, int row, int col) {
        TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
        Object valueToRender = table.getValueAt(row, col);
        boolean isSelected = JTableDriver.arrayContains(table.getSelectedRows(), row);
        boolean hasFocus = isSelected && JTableDriver.arrayContains(table.getSelectedColumns(), col);
        return cellRenderer.getTableCellRendererComponent(table, valueToRender, isSelected, hasFocus, row, col);
    }

    public Component getRenderedCell() {
        return cell;
    }

    public Component getEditorComponent() {
        return editorComponent;
    }
}
