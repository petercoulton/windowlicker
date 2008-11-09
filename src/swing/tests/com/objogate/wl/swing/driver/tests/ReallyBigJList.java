package com.objogate.wl.swing.driver.tests;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

class ReallyBigJList extends JList {
    public ReallyBigJList() {
        super(new FontModel(25));
    }

    public void applyFontRenderer() {
        final ListCellRenderer underlyingRenderer = getCellRenderer();
        setCellRenderer(applyFontRenderer(underlyingRenderer));
    }

    public static ListCellRenderer applyFontRenderer(final ListCellRenderer underlyingRenderer) {
        return new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component rendered = underlyingRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                Font f = (Font) value;

                rendered.setFont(f);

                if (rendered instanceof JLabel) {
                    JLabel jLabel = (JLabel) rendered;
                    jLabel.setText(index + " " + f.getName());
                }

                return rendered;
            }
        };
    }

    private static class FontModel extends AbstractListModel {
        List<Font> fonts;
        
        public FontModel(int maxSize) {
            this.fonts = new BigListOfRandomlySizedFonts().subList(0, maxSize);
        }

        public int getSize() {
            return fonts.size();
        }

        public Object getElementAt(int index) {
            return fonts.get(index);
        }
    }

}
