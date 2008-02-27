package com.objogate.wl.driver.tests;

import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ReallyBigTable extends JTable {
    MutableTableModel tableModel = new MutableTableModel();

    public ReallyBigTable() {
        setName(ReallyBigTable.class.getName());
        for (char c = 'a'; c < 'z'; c++) {
            tableModel.addColumn(c);
        }
        setModel(tableModel);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setIdentifier(getColumnName(i));
        }

        // workaround for jvm bug 6503981 in 1.6
        getTableHeader().addMouseListener(new WorkaroundMouseListener());
    }

    public void stripe(final Color evenRowBackgroundColor, final Color oddRowBackgroundColor, Color textColor) {
        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn tableColumn = getColumnModel().getColumn(i);
            TableCellRenderer cellRenderer = tableColumn.getCellRenderer();
            if (cellRenderer == null) {
                cellRenderer = new DefaultTableCellRenderer();
            }

            TableCellRenderer striped = stripe(cellRenderer, oddRowBackgroundColor, evenRowBackgroundColor, textColor);
            tableColumn.setCellRenderer(striped);
        }
    }

    private TableCellRenderer stripe(final TableCellRenderer underlyingRenderer, final Color oddRowBackgroundColor, final Color evenRowBackgroundColor, final Color textColor) {
        return new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable jTable, Object object, boolean checked, boolean isSelected, int row, int col) {
                Component compnent = underlyingRenderer.getTableCellRendererComponent(jTable, object, checked, checked, row, row);

                compnent.setForeground(textColor);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        compnent.setBackground(evenRowBackgroundColor);
                    } else {
                        compnent.setBackground(oddRowBackgroundColor);
                    }
                }

                return compnent;
            }
        };

    }


    public void addJTextFieldEditorToColumn(int col) {
        tableModel.addEditableColumn(col);
        getColumnModel().getColumn(col).setCellEditor(new DefaultCellEditor(new JTextField()));
    }

    private static class MutableTableModel extends DefaultTableModel {
        Set<Integer> editableColumns = new HashSet<Integer>();
        Map<String, Object> data = new HashMap<String, Object>();

        public Object getValueAt(int row, int col) {
            String key = key(row, col);
            return data.containsKey(key) ? data.get(key) : row + "x" + col;
        }

        public boolean isCellEditable(int row, int col) {
            return editableColumns.contains(col);
        }

        public int getRowCount() {
            return 10000;
        }

        public void addEditableColumn(int col) {
            this.editableColumns.add(col);
        }

        public void setValueAt(Object object, int row, int col) {
            data.put(key(row, col), object);
            fireTableCellUpdated(row, col);
        }

        private String key(int row, int col) {
            return row + "x" + col;
        }
    }

    private class WorkaroundMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {
            int column = getTableHeader().getColumnModel().getColumnIndexAtX(e.getPoint().x);
            if ( column >= 0 ) {
                setColumnSelectionInterval(column, column);
                Action action = getActionMap().get("focusHeader");
                action.actionPerformed(new ActionEvent(ReallyBigTable.this, 0, "focusHeader"));
            }
        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }
    }

}
