package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainPanel extends JPanel{
    private static final int MW  = 320;
    private static final int MH1 = 100;
    private static final int MH2 = 150;
    private final JFrame frame;
    private final JLabel label = new JLabel();
    private final JCheckBox checkbox1 = new JCheckBox();
    private final JCheckBox checkbox2 = new JCheckBox();
    public MainPanel(final JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        frame.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                initFrameSize();
            }
        });
        label.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                label.setText(frame.getSize().toString());
            }
        });
        checkbox1.setAction(new AbstractAction(MW+"*"+MH1+"以下にならないよう制限する") {
            @Override public void actionPerformed(ActionEvent e) {
                initFrameSize();
            }
        });
        checkbox2.setAction(new AbstractAction(MW+"*"+MH2+"以下にならないよう制限する(1.6以上)") {
            @Override public void actionPerformed(ActionEvent e) {
                frame.setMinimumSize((checkbox2.isSelected())?new Dimension(MW, MH2):null);
            }
        });
        checkbox1.setSelected(true);
        checkbox2.setSelected(true);
        frame.setMinimumSize(new Dimension(MW, MH2));

        Box box = Box.createVerticalBox();
        box.add(checkbox1);
        box.add(checkbox2);
        add(box, BorderLayout.NORTH);
        add(label);
        setPreferredSize(new Dimension(320, 240));
    }
    private void initFrameSize() {
        if(!checkbox1.isSelected()) return;
        int fw = frame.getSize().width;
        int fh = frame.getSize().height;
        frame.setSize((MW>fw)?MW:fw, (MH1>fh)?MH1:fh);
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
//         final int MAX = 500;
//         //frame.setMaximumSize(new Dimension(MAX,MAX));
//         Robot r;
//         final Robot r2;
//         try{
//             r = new Robot();
//         }catch(AWTException ex) {
//             r = null;
//         }
//         r2 = r;
//         frame.getRootPane().addComponentListener(new ComponentAdapter() {
//             public void componentResized(ComponentEvent e) {
//                 Point loc   = frame.getLocationOnScreen();
//                 Point mouse = MouseInfo.getPointerInfo().getLocation();
//                 if(r2!=null && (mouse.getX()>loc.getX()+MAX ||
//                                 mouse.getY()>loc.getY()+MAX)) {
//                     r2.mouseRelease(InputEvent.BUTTON1_MASK);
//                     frame.setSize(Math.min(MAX, frame.getWidth()),
//                                   Math.min(MAX, frame.getHeight()));
//                 }
//             }
//         });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}