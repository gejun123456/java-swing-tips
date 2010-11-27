package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class MainPanel extends JPanel {
    //<blockquote cite="FixedColumnExample.java">
    //@auther Nobuo Tamemasa
    private static final Object[][] data = new Object[][] {
        {1,11,"A","","","","",""},
        {2,22,"","B","","","",""},
        {3,33,"","","C","","",""},
        {4, 1,"","","","D","",""},
        {5,55,"","","","","E",""},
        {6,66,"","","","","","F"}};
    private static final Object[] columnNames = new Object[] {
        "fixed 1","fixed 2","a","b","c","d","e","f"};
    //</blockquote>
    private final DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        @Override public Class<?> getColumnClass(int modelIndex) {
            return modelIndex<2?Integer.class:Object.class;
        }
    };
    private final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
    private final JTable fixedTable;
    private final JTable table;
    public MainPanel() {
        super(new BorderLayout());
        fixedTable = new JTable(model);
        table      = new JTable(model);
        fixedTable.setSelectionModel(table.getSelectionModel());

        for(int i=model.getColumnCount()-1;i>=0;i--) {
            if(i<2) {
                table.removeColumn(table.getColumnModel().getColumn(i));
                fixedTable.getColumnModel().getColumn(i).setResizable(false);
            }else{
                fixedTable.removeColumn(fixedTable.getColumnModel().getColumn(i));
            }
        }

        fixedTable.setRowSorter(sorter);
        fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fixedTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        table.setRowSorter(sorter);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        final JScrollPane scroll = new JScrollPane(table);
//         JViewport viewport = new JViewport();
//         viewport.setView(fixedTable);
//         viewport.setPreferredSize(fixedTable.getPreferredSize());
//         scroll.setRowHeader(viewport);

        fixedTable.setPreferredScrollableViewportSize(fixedTable.getPreferredSize());
        scroll.setRowHeaderView(fixedTable);
        scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable.getTableHeader());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getRowHeader().setBackground(Color.WHITE);

        //<blockquote cite="http://tips4java.wordpress.com/2008/11/05/fixed-column-table/">
        //@auther Rob Camick
        scroll.getRowHeader().addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                JViewport viewport = (JViewport) e.getSource();
                scroll.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
            }
        });
        //</blockquote>
        add(scroll);
        add(new JButton(new AbstractAction("add") {
            @Override public void actionPerformed(ActionEvent e) {
                sorter.setSortKeys(null);
                for(int i=0;i<100;i++) {
                    model.addRow(new Object[] {
                        i, i+1, "A"+i, "B"+i
                    });
                }
            }
        }), BorderLayout.SOUTH);
        setPreferredSize(new Dimension(320, 180));
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //for(UIManager.LookAndFeelInfo laf: UIManager.getInstalledLookAndFeels()) {
            //    if("Nimbus".equals(laf.getName())) UIManager.setLookAndFeel(laf.getClassName());
            //}
        }catch(Exception e) {
            e.printStackTrace();
        }
        final JFrame frame = new JFrame("FixedColumnTableSorting");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}