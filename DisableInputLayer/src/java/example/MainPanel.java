package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.plaf.LayerUI;

public class MainPanel extends JPanel {
    public JComponent makeUI() {
        final JPanel p = new JPanel();
        final DisableInputLayerUI layerUI = new DisableInputLayerUI();
        final JLayer<JPanel> jlayer = new JLayer<JPanel>(p, layerUI);
        final Timer stopper = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layerUI.stop();
            }
        });
        p.add(new JCheckBox());
        p.add(new JTextField(10));
        p.add(new JButton(new AbstractAction("Stop 5sec") {
            public void actionPerformed(ActionEvent e) {
                layerUI.start();
                if (!stopper.isRunning()) {
                    stopper.start();
                }
            }
        }));
        stopper.setRepeats(false);
        return jlayer;
    }
    public MainPanel() {
        super(new BorderLayout());
        add(makeUI(), BorderLayout.NORTH);
        add(new JScrollPane(new JTextArea("dummy")));
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

class DisableInputLayerUI extends LayerUI<JPanel> {
    private boolean isRunning = false;
    @Override public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        if(!isRunning) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
        g2.setPaint(Color.GRAY);
        g2.fillRect(0, 0, c.getWidth(), c.getHeight());
        g2.dispose();
    }
    @Override public void installUI(JComponent c) {
        super.installUI(c);
        JLayer jlayer = (JLayer)c;
        jlayer.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jlayer.setLayerEventMask(
            AWTEvent.MOUSE_EVENT_MASK |
            AWTEvent.MOUSE_MOTION_EVENT_MASK |
            AWTEvent.KEY_EVENT_MASK);
    }
    @Override public void uninstallUI(JComponent c) {
        JLayer jlayer = (JLayer)c;
        jlayer.setLayerEventMask(0);
        super.uninstallUI(c);
    }
    @Override public void eventDispatched(AWTEvent e, JLayer l) {
        if(isRunning && e instanceof InputEvent) {
            ((InputEvent)e).consume();
        }
    }
    private static final String CMD_REPAINT = "repaint";
    public void start() {
        if (isRunning) return;
        isRunning = true;
        firePropertyChange(CMD_REPAINT,false,true);
    }
    public void stop() {
        isRunning = false;
        firePropertyChange(CMD_REPAINT,true,false);
    }
    @Override public void applyPropertyChange(PropertyChangeEvent pce, JLayer l) {
        String cmd = pce.getPropertyName();
        if(CMD_REPAINT.equals(cmd)) {
            l.getGlassPane().setVisible((Boolean)pce.getNewValue());
            l.repaint();
        }
    }
}