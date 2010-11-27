package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class MainPanel extends JPanel {
    private final JRadioButton r1    = new JRadioButton("scrollRectToVisible");
    private final JRadioButton r2    = new JRadioButton("setViewPosition");
    private final JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                                                       JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private final JViewport viewport = scroll.getViewport();
    private final JLabel label;

    public MainPanel() {
        super(new BorderLayout());
        label = new JLabel(new ImageIcon(getClass().getResource("CRW_3857_JFR.jpg")));
        viewport.add(label);
        final KineticScrollingListener1 l1 = new KineticScrollingListener1(label);
        final KineticScrollingListener2 l2 = new KineticScrollingListener2(label);

        ActionListener al = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if(e.getSource()==r1) {
                    viewport.removeMouseListener(l2);
                    viewport.removeMouseMotionListener(l2);
                    viewport.removeHierarchyListener(l2);
                    viewport.addMouseMotionListener(l1);
                    viewport.addMouseListener(l1);
                    viewport.addHierarchyListener(l1);
                }else{
                    viewport.removeMouseListener(l1);
                    viewport.removeMouseMotionListener(l1);
                    viewport.removeHierarchyListener(l1);
                    viewport.addMouseMotionListener(l2);
                    viewport.addMouseListener(l2);
                    viewport.addHierarchyListener(l2);
                }
            }
        };
        Box box = Box.createHorizontalBox();
        ButtonGroup bg = new ButtonGroup();
        box.add(r1); bg.add(r1); r1.addActionListener(al);
        box.add(r2); bg.add(r2); r2.addActionListener(al);
        r1.setSelected(true);
        viewport.addMouseMotionListener(l1);
        viewport.addMouseListener(l1);
        viewport.addHierarchyListener(l1);

        add(scroll);
        add(box, BorderLayout.NORTH);
        scroll.setPreferredSize(new Dimension(320, 240));
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
        //frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        //frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class KineticScrollingListener1 extends MouseAdapter implements HierarchyListener{
    private static final int SPEED = 6;
    private static final double D = 0.8;
    private final Cursor dc;
    private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private final javax.swing.Timer scroller;
    private final JComponent label;
    private Point startPt = new Point();
    private Point delta   = new Point();

    public KineticScrollingListener1(JComponent comp) {
        this.label = comp;
        this.dc = comp.getCursor();
        this.scroller = new javax.swing.Timer(20, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JViewport vport = (JViewport)label.getParent();
                Point vp = vport.getViewPosition();
                vp.translate(-delta.x, -delta.y);
                label.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
                //System.out.println(delta);
                if(Math.abs(delta.x)>0 || Math.abs(delta.y)>0) {
                    delta.setLocation((int)(delta.x*D), (int)(delta.y*D));
                }else{
                    scroller.stop();
                }
            }
        });
    }
    @Override public void mousePressed(MouseEvent e) {
        label.setCursor(hc);
        startPt.setLocation(e.getPoint());
        scroller.stop();
    }
    @Override public void mouseDragged(MouseEvent e) {
        Point pt = e.getPoint();
        JViewport vport = (JViewport)label.getParent();
        Point vp = vport.getViewPosition(); //= SwingUtilities.convertPoint(vport,0,0,label);
        vp.translate(startPt.x-pt.x, startPt.y-pt.y);
        delta.setLocation(SPEED*(pt.x-startPt.x), SPEED*(pt.y-startPt.y));
        label.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
        startPt.setLocation(pt);
    }
    @Override public void mouseReleased(MouseEvent e) {
        label.setCursor(dc);
        scroller.start();
    }
    @Override public void hierarchyChanged(HierarchyEvent e) {
        JComponent c = (JComponent)e.getSource();
        if((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED)!=0 && !c.isDisplayable()) {
            scroller.stop();
        }
    }
}

class KineticScrollingListener2 extends MouseAdapter implements HierarchyListener{
    private static final int SPEED = 6;
    private static final double D = 0.7;
    private final Cursor dc;
    private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private final javax.swing.Timer inside;
    private final javax.swing.Timer outside;
    private final JComponent label;
    private Point startPt = new Point();
    private Point delta   = new Point();
    private static boolean isInside(JViewport vport, JComponent comp) {
        Point vp = vport.getViewPosition();
        return (vp.x>=0 && vp.x+vport.getWidth()-comp.getWidth()<=0 &&
                vp.y>=0 && vp.y+vport.getHeight()-comp.getHeight()<=0);
    }
    public KineticScrollingListener2(JComponent comp) {
        this.label = comp;
        this.dc = comp.getCursor();
        this.inside = new javax.swing.Timer(20, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JViewport vport = (JViewport)label.getParent();
                Point vp = vport.getViewPosition();
                //System.out.format("s: %s, %s\n", delta, vp);
                vp.translate(-delta.x, -delta.y);
                vport.setViewPosition(vp);

                if(Math.abs(delta.x)>0 || Math.abs(delta.y)>0) {
                    delta.setLocation((int)(delta.x*D), (int)(delta.y*D));
                    //Outside
                    if(vp.x<0 || vp.x+vport.getWidth()-label.getWidth()>0  ) delta.x = (int)(delta.x*D);
                    if(vp.y<0 || vp.y+vport.getHeight()-label.getHeight()>0) delta.y = (int)(delta.y*D);
                } else {
                    inside.stop();
                    if(!isInside(vport, label)) outside.start();
                }
            }
        });
        this.outside = new javax.swing.Timer(20, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JViewport vport = (JViewport)label.getParent();
                Point vp = vport.getViewPosition();
                //System.out.format("r: %s\n", vp);
                if(vp.x<0) vp.x = (int)(vp.x*D);
                if(vp.y<0) vp.y = (int)(vp.y*D);
                if(vp.x+vport.getWidth()-label.getWidth()>0)
                  vp.x = (int) (vp.x - (vp.x+vport.getWidth()-label.getWidth())*(1.0-D));
                if(vp.y+vport.getHeight()>label.getHeight())
                  vp.y = (int) (vp.y - (vp.y+vport.getHeight()-label.getHeight())*(1.0-D));
                vport.setViewPosition(vp);
                if(isInside(vport, label)) outside.stop();
            }
        });
    }
    @Override public void mousePressed(MouseEvent e) {
        label.setCursor(hc);
        startPt.setLocation(e.getPoint());
        inside.stop();
        outside.stop();
    }
    @Override public void mouseDragged(MouseEvent e) {
        Point pt = e.getPoint();
        JViewport vport = (JViewport)label.getParent();
        Point vp = vport.getViewPosition();
        vp.translate(startPt.x-pt.x, startPt.y-pt.y);
        vport.setViewPosition(vp);
        delta.setLocation(SPEED*(pt.x-startPt.x), SPEED*(pt.y-startPt.y));
        startPt.setLocation(pt);
    }
    @Override public void mouseReleased(MouseEvent e) {
        label.setCursor(dc);
        if(isInside((JViewport)label.getParent(), label)) {
            inside.start();
        }else{
            outside.start();
        }
    }
    @Override public void hierarchyChanged(HierarchyEvent e) {
        JComponent c = (JComponent)e.getSource();
        if((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED)!=0 && !c.isDisplayable()) {
            inside.stop();
            outside.stop();
        }
    }
}