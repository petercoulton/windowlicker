package com.objogate.wl.swing.driver.tests;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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

    private interface TableColumnVisitor {
        void visit(TableColumn column, TableCellRenderer cellRenderer);
    }

    public void stripe(final Color evenRowBackgroundColor, final Color oddRowBackgroundColor, final Color textColor) {
        foreachTableColumn(new TableColumnVisitor() {
            public void visit(TableColumn column, TableCellRenderer cellRenderer) {
                column.setCellRenderer(stripe(cellRenderer, oddRowBackgroundColor, evenRowBackgroundColor, textColor));
            }
        });
    }

    public void pad() {
        foreachTableColumn(new TableColumnVisitor() {
            public void visit(TableColumn column, TableCellRenderer cellRenderer) {
                column.setCellRenderer(pad(cellRenderer));
            }
        });
    }

    private void foreachTableColumn(TableColumnVisitor tableColumnVisitor) {
        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn tableColumn = getColumnModel().getColumn(i);
            TableCellRenderer cellRenderer = tableColumn.getCellRenderer();
            if (cellRenderer == null) {
                cellRenderer = new DefaultTableCellRenderer();
            }
            tableColumnVisitor.visit(tableColumn, cellRenderer);
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

    private TableCellRenderer pad(final TableCellRenderer underlyingRenderer) {
        return new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable jTable, Object object, boolean checked, boolean isSelected, int row, int col) {
                Component compnent = underlyingRenderer.getTableCellRendererComponent(jTable, object, checked, checked, row, row);
                if (compnent instanceof JLabel) {
                    JLabel jLabel = (JLabel) compnent;
                    jLabel.setBorder(new CompoundBorder(jLabel.getBorder(), new EmptyBorder(0, 8, 0, 8)));
                }
                return compnent;
            }
        };

    }

    public void addJTextFieldEditorToColumn(int col) {
        getColumnModel().getColumn(col).setCellEditor(new DefaultCellEditor(new JTextField()));
    }

    private static class MutableTableModel extends DefaultTableModel {
        Map<String, Object> data = new HashMap<String, Object>();

        @Override
        public Object getValueAt(int row, int col) {
            String key = key(row, col);
            return data.containsKey(key) ? data.get(key) : row + "x" + col;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        @Override
        public int getRowCount() {
            return 10000;
        }

        @Override
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
                if (action != null) {
                    action.actionPerformed(new ActionEvent(ReallyBigTable.this, 0, "focusHeader"));
                }
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
