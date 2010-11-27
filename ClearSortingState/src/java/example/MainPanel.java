package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class MainPanel extends JPanel {
    private final TestModel model = new TestModel();
    private final JTable table = new JTable(model);
    public MainPanel() {
        super(new BorderLayout());

        table.setAutoCreateRowSorter(true);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                final RowSorter<? extends TableModel> sorter = table.getRowSorter();
                if(sorter==null || sorter.getSortKeys().size()==0) return;

                JTableHeader h = (JTableHeader)e.getComponent();
                TableColumnModel columnModel = h.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                // ArrayIndexOutOfBoundsException: -1
                if(viewColumn<0) return;
                int column = columnModel.getColumn(viewColumn).getModelIndex();

                if(column != -1 && e.isShiftDown()) {
                //if(column != -1 && e.isControlDown()) {
                    EventQueue.invokeLater(new Runnable() {
                        @Override public void run() {
                            sorter.setSortKeys(null);
                        }
                    });
                }
            }
        });

        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMinWidth(60);
        col.setMaxWidth(60);
        col.setResizable(false);

        model.addTest(new Test("Name 1", "Comment"));
        model.addTest(new Test("Name 2", "Test"));
        model.addTest(new Test("Name d", ""));
        model.addTest(new Test("Name c", "Test cc"));
        model.addTest(new Test("Name b", "Test bb"));
        model.addTest(new Test("Name a", ""));
        model.addTest(new Test("Name 0", "Test aa"));
        model.addTest(new Test("Name 0", ""));

        add(new JLabel("Shift+Click -> Clear Sorting State"), BorderLayout.NORTH);
        add(new JScrollPane(table));
        setPreferredSize(new Dimension(320, 240));
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
        }catch(Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}