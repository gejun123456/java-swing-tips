package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

class MainPanel extends JPanel{
    private final TexturePaint texture;

    public MainPanel() {
        super(new BorderLayout());
        BufferedImage bi = null;
        try{
            bi = ImageIO.read(getClass().getResource("16x16.png"));
        }catch(java.io.IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        }
        texture = new TexturePaint(bi, new Rectangle(bi.getWidth(),bi.getHeight()));

        add(new JLabel("@title@"));
        setOpaque(false);
        setPreferredSize(new Dimension(320, 180));
    }
    @Override public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(texture);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
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