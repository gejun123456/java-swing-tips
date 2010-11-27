package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

class MainPanel extends JPanel {
    private final MyTabbedPane tab = new MyTabbedPane();
    public MainPanel() {
        super(new BorderLayout());
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ImageIcon icon = new ImageIcon(getClass().getResource("wi0124-48.png"));
        tab.addTab("wi0124-48.png", null, new JLabel(icon), "dummy");
        //addImageTab(tab, getClass().getResource("wi0124-48.png"));
        addImageTab(tab, getClass().getResource("tokeidai.jpg"));
        addImageTab(tab, getClass().getResource("CRW_3857_JFR.jpg"));
        add(tab);
        setPreferredSize(new Dimension(320,240));
    }
    private void addImageTab(JTabbedPane tabs, URL url) {
        JScrollPane scroll = new JScrollPane(new JLabel(new ImageIcon(url)));
        File f = new File(url.getFile());
        tabs.addTab(f.getName(), null, scroll, "dummy");
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

class MyTabbedPane extends JTabbedPane {
    private int current = -1;
    private static final double SCALE = 0.15d;
    private Component getTabThumbnail(int index) {
        Component c = getComponentAt(index);
        Icon icon = null;
        if(c instanceof JScrollPane) {
            c = ((JScrollPane)c).getViewport().getView();
            Dimension d = c.getPreferredSize();
            int newW = (int)(d.width  * SCALE);
            int newH = (int)(d.height * SCALE);
            BufferedImage image = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D)image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.scale(SCALE,SCALE);
            c.paint(g2);
            g2.dispose();
            icon = new ImageIcon(image);
        }else if(c instanceof JLabel) {
            icon = ((JLabel)c).getIcon();
        }
        return new JLabel(icon);
    }
    @Override public JToolTip createToolTip() {
        int index = current;
        if(index<0) return null;

        final JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder());
        p.add(new JLabel(getTitleAt(index)), BorderLayout.NORTH);
        p.add(getTabThumbnail(index));

        JToolTip tip = new JToolTip() {
            @Override public Dimension getPreferredSize() {
                Insets i = getInsets();
                Dimension d = p.getPreferredSize();
                return new Dimension(d.width  + i.left + i.right, d.height + i.top  + i.bottom);
            }
        };
        tip.setComponent(this);
        LookAndFeel.installColorsAndFont(p, "ToolTip.background", "ToolTip.foreground", "ToolTip.font");
        tip.setLayout(new BorderLayout());
        tip.add(p);
        return tip;
    }
    @Override public String getToolTipText(MouseEvent e) {
        int index = indexAtLocation(e.getX(), e.getY());
        String str = (current!=index)?null:super.getToolTipText(e);
        current = index;
        return str;
    }
}