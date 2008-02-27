package com.objogate.wl.driver.tests;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

class ReallyBigJList extends JList {
    public ReallyBigJList() {
        super(new FontModel());
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
        List fonts;

        public FontModel() {
            ArrayList<Font> list = new BigListOfRandomlySizedFonts();

            this.fonts = list;
        }

        public int getSize() {
            return fonts.size();
        }

        public Object getElementAt(int index) {
            return fonts.get(index);
        }

    }

}
