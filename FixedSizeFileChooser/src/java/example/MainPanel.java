package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        super(new GridLayout(2,1));
        final JPanel p1 = new JPanel();
        p1.setBorder(BorderFactory.createTitledBorder("JFileChooser setResizable"));
        p1.add(new JButton(new AbstractAction("Default") {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int retvalue = fileChooser.showOpenDialog(p1);
                System.out.println(retvalue);
            }
        }));
        p1.add(new JButton(new AbstractAction("Resizable(false)") {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser() {
                    protected JDialog createDialog(Component parent) throws HeadlessException {
                        JDialog dialog = super.createDialog(parent);
                        dialog.setResizable(false);
                        return dialog;
                    }
                };
                int retvalue = fileChooser.showOpenDialog(p1);
                System.out.println(retvalue);
            }
        }));

        final JPanel p2 = new JPanel();
        p2.setBorder(BorderFactory.createTitledBorder("JFileChooser setMinimumSize"));
        p2.add(new JButton(new AbstractAction("MinimumSize(640,480)(JDK 6)") {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser() {
                    protected JDialog createDialog(Component parent) throws HeadlessException {
                        JDialog dialog = super.createDialog(parent);
                        dialog.setMinimumSize(new Dimension(640,480));
                        return dialog;
                    }
                };
                int retvalue = fileChooser.showOpenDialog(p2);
                System.out.println(retvalue);
            }
        }));
        p2.add(new JButton(new AbstractAction("MinimumSize(640,480)") {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser() {
                    protected JDialog createDialog(Component parent) throws HeadlessException {
                        final JDialog dialog = super.createDialog(parent);
                        dialog.addComponentListener(new ComponentAdapter() {
                            @Override public void componentResized(ComponentEvent e) {
                                int mw = 640;
                                int mh = 480;
                                int fw = dialog.getSize().width;
                                int fh = dialog.getSize().height;
                                dialog.setSize((mw>fw)?mw:fw, (mh>fh)?mh:fh);
                            }
                        });
                        return dialog;
                    }
                };
                int retvalue = fileChooser.showOpenDialog(p2);
                System.out.println(retvalue);
            }
        }));
        add(p1);
        add(p2);
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