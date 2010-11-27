package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainPanel extends JPanel {
    private final JButton button = new JButton("Stop 5sec");
    public MainPanel(final JFrame frame) {
        super(new BorderLayout());
        frame.setGlassPane(new LockingGlassPane());
        frame.getGlassPane().setVisible(false);
        button.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                //System.out.println("actionPerformed: " + EventQueue.isDispatchThread());
                frame.getGlassPane().setVisible(true);
                button.setEnabled(false);
                new SwingWorker() {
                    @Override public Object doInBackground() {
                        dummyLongTask();
                        return "Done";
                    }
                    @Override public void done() {
                        frame.getGlassPane().setVisible(false);
                        button.setEnabled(true);
                    }
                }.execute();
            }
        });
        Box box = Box.createHorizontalBox();
        box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton b = new JButton("Button&Mnemonic");
        b.setMnemonic(KeyEvent.VK_B);

        JTextField t = new JTextField("TextField&ToolTip");
        t.setToolTipText("ToolTip");

        box.add(b); box.add(Box.createHorizontalStrut(5));
        box.add(t); box.add(Box.createHorizontalStrut(5));

        add(box, BorderLayout.NORTH);
        add(button, BorderLayout.SOUTH);
        add(new JScrollPane(new JTree()));
        setPreferredSize(new Dimension(320, 200));
    }

    public void dummyLongTask() {
        try{
            Thread.sleep(5000);
        }catch(InterruptedException ex) {
            ex.printStackTrace();
        }
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

class LockingGlassPane extends JComponent {
    public LockingGlassPane() {
        setOpaque(false);
        super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    @Override public void setVisible(boolean isVisible) {
        boolean oldVisible = isVisible();
        super.setVisible(isVisible);
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        if(rootPane!=null && isVisible()!=oldVisible) {
            rootPane.getLayeredPane().setVisible(!isVisible);
        }
    }
    @Override public void paintComponent(Graphics g) {
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        if(rootPane!=null) {
            // http://weblogs.java.net/blog/alexfromsun/archive/2008/01/disabling_swing.html
            // it is important to call print() instead of paint() here
            // because print() doesn't affect the frame's double buffer
            rootPane.getLayeredPane().print(g);
        }
        super.paintComponent(g);
    }
}

//     class LockingGlassPane_ extends JComponent {
//         public LockingGlassPane_() {
//             setOpaque(false);
//             setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
//                 public boolean accept(Component c) {return false;}
//             });
// //             Set<AWTKeyStroke> s = Collections.emptySet();
// //             setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, s);
// //             setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, s);
//             //addKeyListener(new KeyAdapter() {});
//             addMouseListener(new MouseAdapter() {});
//             requestFocusInWindow();
//             super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//         }
//         @Override
//         public void setVisible(boolean flag) {
//             super.setVisible(flag);
//             setFocusTraversalPolicyProvider(flag);
//         }
//     }