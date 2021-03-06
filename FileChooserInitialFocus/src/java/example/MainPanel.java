package example;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.swing.*;
//import javax.swing.event.*;

public final class MainPanel extends JPanel {
    private final JTextField field = new JTextField("C:/temp/test.txt");
    private final JRadioButton r1  = new JRadioButton("default", false);
    private final JRadioButton r2  = new JRadioButton("set initial focus on JTextField", true);
    private final JButton button   = new JButton("JFileChooser");
    private final JTextArea log    = new JTextArea();
    private final JPanel p         = new JPanel(new BorderLayout());
    //private final JFileChooser fileChooser;

    public MainPanel() {
        super(new BorderLayout());

        JFileChooser fileChooser = new JFileChooser();

//         //TEST: PropertyChangeListener
//         fileChooser.addPropertyChangeListener(e -> {
//             String s = e.getPropertyName();
//             if (s.equals("ancestor")) {
//                 if (e.getOldValue() == null && e.getNewValue() != null) {
//                     // Ancestor was added, set initial focus
//                     findFileNameTextField(fileChooser).ifPresent(c -> {
//                         ((JTextField) c).selectAll();
//                         c.requestFocusInWindow();
//                     });
//                 }
//             }
//         });

//         //TEST: AncestorListener
//         fileChooser.addAncestorListener(new AncestorListener() {
//             @Override public void ancestorAdded(AncestorEvent e) {
//                 findFileNameTextField(fileChooser).ifPresent(c -> {
//                     ((JTextField) c).selectAll();
//                     c.requestFocusInWindow();
//                 });
//             }
//             @Override public void ancestorMoved(AncestorEvent e) {}
//             @Override public void ancestorRemoved(AncestorEvent e) {}
//         });

//         //TEST: doAncestorChanged
//         fileChooser = new JFileChooser() {
//             @Override public void updateUI() {
//                 super.updateUI();
//                 EventQueue.invokeLater(() -> {
//                 setUI(new sun.swing.plaf.synth.SynthFileChooserUIImpl(fileChooser) {
//                     @Override protected void doAncestorChanged(java.beans.PropertyChangeEvent e) {
//                         findFileNameTextField(fileChooser).ifPresent(c -> {
//                             ((JTextField) c).selectAll();
//                             c.requestFocusInWindow();
//                         });
//                     }
//                 });
//                 });
//             }
//         };

        button.addActionListener(e -> {
            fileChooser.setSelectedFile(new File(field.getText().trim()));
            if (r2.isSelected()) {
                EventQueue.invokeLater(() -> {
                    findFileNameTextField(fileChooser).ifPresent(c -> {
                        ((JTextField) c).selectAll();
                        c.requestFocusInWindow();
                    });
                });
            }
            int retvalue = fileChooser.showOpenDialog(SwingUtilities.getRoot(p));
            if (retvalue == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                field.setText(path);
                log.append(path + "\n");
            }
        });

        JPanel p1 = new JPanel(new BorderLayout(5, 5));
        p1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        p1.add(field);
        p1.add(button, BorderLayout.EAST);

        ButtonGroup bg = new ButtonGroup();
        JPanel p2 = new JPanel();
        for (AbstractButton b: Arrays.asList(r1, r2)) {
            bg.add(b);
            p2.add(b);
        }

        p.add(p1, BorderLayout.NORTH);
        p.add(p2, BorderLayout.SOUTH);
        add(p, BorderLayout.NORTH);
        add(new JScrollPane(log));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setPreferredSize(new Dimension(320, 240));
    }
    private static Optional<Component> findFileNameTextField(JFileChooser fileChooser) {
        return Arrays.stream(fileChooser.getComponents())
                     .flatMap(new Function<Component, Stream<Component>>() {
                         @Override public Stream<Component> apply(Component c) {
                             if (c instanceof Container) {
                                 Component[] sub = ((Container) c).getComponents();
                                 return sub.length == 0 ? Stream.of(c)
                                                        : Arrays.stream(sub).flatMap(cc -> apply(cc));
                             } else {
                                 return Stream.of(c);
                             }
                         }
                     })
                     .filter(c -> c instanceof JTextField)
                     .findFirst();
    }
    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (UIManager.LookAndFeelInfo laf: UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(laf.getName())) {
                    UIManager.setLookAndFeel(laf.getClassName());
                }
            }
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
