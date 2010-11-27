package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MainPanel extends JPanel{
    private final JLabel lbl1 = new JLabel("aaaaaaaaaa");
    private final JLabel lbl2 = new JLabel("bbbbbbbbbb");
    private final JScrollPane sp1 = new JScrollPane(lbl1);
    private final JScrollPane sp2 = new JScrollPane(lbl2);
    public MainPanel() {
        super();
        lbl1.setPreferredSize(new Dimension(1200, 600));
        lbl2.setPreferredSize(new Dimension(600, 1200));
        setLayout(new GridLayout(2,1));
        add(sp1);
        add(sp2);
        setPreferredSize(new Dimension(500, 500));

        ChangeListener cl = new ChangeListener() {
            boolean adjflg = false;
            @Override public void stateChanged(ChangeEvent e) {
                JViewport src = null;
                JViewport tgt = null;
                if(e.getSource()==sp1.getViewport()) {
                    src = sp1.getViewport();
                    tgt = sp2.getViewport();
                }else if(e.getSource()==sp2.getViewport()) {
                    src = sp2.getViewport();
                    tgt = sp1.getViewport();
                }
                if(adjflg || tgt==null || src==null) return;
                adjflg = true;
                Dimension dim1 = src.getViewSize();
                Dimension siz1 = src.getSize();
                Point     pnt1 = src.getViewPosition();
                Dimension dim2 = tgt.getViewSize();
                Dimension siz2 = tgt.getSize();
                //Point     pnt2 = tgt.getViewPosition();
                double d;
                d = pnt1.getY()/(dim1.getHeight()-siz1.getHeight())*(dim2.getHeight()-siz2.getHeight());
                pnt1.y = (int)d;
                d = pnt1.getX()/(dim1.getWidth()-siz1.getWidth())*(dim2.getWidth()-siz2.getWidth());
                pnt1.x = (int)d;
                tgt.setViewPosition(pnt1);
                adjflg = false;
            }
        };
        sp1.getViewport().addChangeListener(cl);
        sp2.getViewport().addChangeListener(cl);
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