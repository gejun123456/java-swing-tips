package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainPanel extends JPanel{
    private final JFrame frame;
    private final JLabel label = new JLabel();
    private final JCheckBox checkbox = new JCheckBox();
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
        checkbox.setAction(new AbstractAction("縦横比一定、"+MW+"*"+MH+"以下は不可にする") {
            @Override public void actionPerformed(ActionEvent e) {
                initFrameSize();
            }
        });
        checkbox.setSelected(true);
        add(checkbox, BorderLayout.NORTH);
        add(label);
        setPreferredSize(new Dimension(320, 240));
    }
    private static final int MW = 300;
    private static final int MH = 200;
    private void initFrameSize() {
        if(!checkbox.isSelected()) return;
        int fw = frame.getSize().width;
        int fh = MH*fw/MW;
        frame.setSize((MW>fw)?MW:fw, (MH>fh)?MH:fh);
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
        frame.getContentPane().add(new MainPanel(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}