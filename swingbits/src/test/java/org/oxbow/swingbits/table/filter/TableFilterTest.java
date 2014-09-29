/*
 * Copyright (c) 2009-2011, EzWare
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.Redistributions
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.Neither the name of the
 * EzWare nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


package org.oxbow.swingbits.table.filter;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class TableFilterTest implements Runnable {

    
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater( new TableFilterTest() );
        
    }


    @Override
    public void run() {
        
        JFrame f = new JFrame("Swing Table Filter Test");
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setPreferredSize( new Dimension( 1000, 600 ));
        
        JPanel p = (JPanel) f.getContentPane();
        p.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5));
        final JTable table = buildTable();
        p.add( new JScrollPane( table ));
        
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
    }
    
    private JTable buildTable() {
        JTable table = TableRowFilterSupport.forTable(new JTable()).actions(true).searchable(true).useTableRenderers(true).apply();
        //JTable table = new JTable();
        table.setModel( new DefaultTableModel(data, colNames) );
        table.getColumnModel().getColumn(0).setCellRenderer(new TestRenderer());
        return table;
    }
    
    private static final int ITEM_COUNT = 2000;

    public static Object[] colNames = { "A123\nte", "B123\nte", "C123" };

    public static Object[][] sample = {

        { "aaa444", 123.2, "ccc333" },
        {    null,  88888888,    null },
        { "aaa333", 12344, "ccc222" },
        { "aaa333", 67456.34534, "ccc111" },
        { "aaa111", 78427.33, "ccc444" }

    };

    public static Object[][] data = new Object[ITEM_COUNT][sample[0].length];

    static {

        for( int i = 0; i<ITEM_COUNT; i+=sample.length ) {
            System.arraycopy(sample, 0, data, i, sample.length);
        }

    }
    
    @SuppressWarnings("serial")
    static class TestRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, 
                int row, int column) {
        
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText( getText() + "  ********" );
            return this;
        }
        
    }


    class MultiLineHeaderRenderer extends JPanel implements TableCellRenderer {

        private final JList<String> lineList = new JList<String>();
        private final JLabel iconLabel = new JLabel(" ");
        private final boolean autoWrap;

        public MultiLineHeaderRenderer(boolean autoWrap) {
            super(new BorderLayout());
            this.autoWrap = autoWrap;

            this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

            lineList.setBackground(UIManager.getColor("TableHeader.background"));
            iconLabel.setBackground(UIManager.getColor("TableHeader.background"));

            final JLabel renderer = (JLabel) lineList.getCellRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
            lineList.setFont(lineList.getFont().deriveFont(Font.PLAIN));

            add(lineList, BorderLayout.CENTER);
            add(iconLabel, BorderLayout.EAST);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            String str = value.toString();

            if (table.getRowSorter() != null) {
                handleSortingIcons(table, column);
            }

            String[] lines = str.split("\n");
            lineList.setListData(lines);
            return this;
        }

        private void handleSortingIcons(JTable table, int column) {
            iconLabel.setIcon(null);
            Icon ascIcon = UIManager.getIcon("Table.ascendingSortIcon");
            Icon descIcon = UIManager.getIcon("Table.descendingSortIcon");
            java.util.List<? extends RowSorter.SortKey> sortKeys =
                    table.getRowSorter().getSortKeys();
            for (RowSorter.SortKey sortKey : sortKeys) {
                if (sortKey.getColumn() == table.convertColumnIndexToModel(column)) {
                    SortOrder o = sortKey.getSortOrder();
                    iconLabel.setIcon(o == SortOrder.ASCENDING ? ascIcon : descIcon);
                    break;
                }
            }
        }
    }
}
