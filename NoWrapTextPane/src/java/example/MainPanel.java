package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

public class MainPanel extends JPanel{
    private final JPanel panel = new JPanel(new GridLayout(2,1));
//     private final JPanel panel = new JPanel(new GridLayout(3,1));
//     private final JTextPane   textPane;
    private final JEditorPane editorPane;
    private final JTextArea   textArea;
    private final ExecutorService threadPool;
    //private String text = null;
    public MainPanel(final ExecutorService threadPool) {
        super(new BorderLayout());
        this.threadPool = threadPool;
//         textPane = new JTextPane() {
//             //Non Wrapping(Wrap) TextPane : TextField : Swing JFC : Java examples (example source code) Organized by topic
//             //http://www.java2s.com/Code/Java/Swing-JFC/NonWrappingWrapTextPane.htm
//             @Override
//             public boolean getScrollableTracksViewportWidth() {
//                 Component p = getParent();
//                 if(p==null) return true;
//                 int ewidth = getUI().getPreferredSize(this).width;
//                 return ewidth<=p.getSize().width;
//             }
//         };
        editorPane = new JEditorPane();
        textArea = new JTextArea();

        //textPane.setEditorKit(new NoWrapEditorKit1());
        editorPane.setEditorKit(new NoWrapEditorKit2());

        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
//         box.add(new JButton(new AbstractAction("JTextPane") {
//             public void actionPerformed(ActionEvent e) {
//                 textPane.setText(text);
//             }
//         }));
        box.add(new JButton(new AbstractAction("JEditorPane") {
            @Override public void actionPerformed(ActionEvent e) {
                threadPool.execute(new Runnable() {
                    @Override public void run() {
                        if(text!=null)
                          editorPane.setText(text);
                    }
                });
            }
        }));
        box.add(new JButton(new AbstractAction("JTextArea") {
            @Override public void actionPerformed(ActionEvent e) {
                threadPool.execute(new Runnable() {
                    @Override public void run() {
                        if(text!=null)
                          textArea.setText(text);
                    }
                });
            }
        }));
        box.add(new JButton(new AbstractAction("clear all") {
            @Override public void actionPerformed(ActionEvent e) {
                editorPane.setText("");
                textArea.setText("");
            }
        }));

        //addEditor(textPane, "NoWrapTextPane(JTextPane)");
        addEditor(editorPane, "NoWrapEditorKit(JEditorPane)");
        addEditor(textArea, "JTextArea");

        //System.out.println(textPane.getDocument().toString());
        //textPane.setText(text);

        add(box, BorderLayout.NORTH);
        add(panel);
        setPreferredSize(new Dimension(320, 240));
    }
    private void addEditor(JComponent c, String title) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(sp);
    }
    private static String text = null;
    private static void initLongLineStringInBackground(ExecutorService threadPool, final int length) {
        threadPool.execute(new Runnable() {
            @Override public void run() {
                StringBuffer sb = new StringBuffer(length);
                for(int i=0;i<length-2;i++) {
                    sb.append('a');
                }
                sb.append("x\n");
                text = sb.toString();
            }
        });
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
        ExecutorService threadPool = Executors.newCachedThreadPool();
        initLongLineStringInBackground(threadPool, 1024 * 1024);
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel(threadPool));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Swing - Disabling word wrap for JTextPane
//http://forums.sun.com/thread.jspa?threadID=622683
class NoWrapParagraphView extends ParagraphView {
    public NoWrapParagraphView(Element elem) {
        super(elem);
    }
    protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
        SizeRequirements req = super.calculateMinorAxisRequirements(axis, r);
        req.minimum = req.preferred;
        return req;
    }
    public int getFlowSpan(int index) {
        return Integer.MAX_VALUE;
    }
}
class NoWrapViewFactory implements ViewFactory {
    public View create(Element elem) {
        String kind = elem.getName();
        if(kind != null) {
            if(kind.equals(AbstractDocument.ContentElementName)) {
                return new LabelView(elem);
            }else if(kind.equals(AbstractDocument.ParagraphElementName)) {
                return new NoWrapParagraphView(elem);
            }else if(kind.equals(AbstractDocument.SectionElementName)) {
                return new BoxView(elem, View.Y_AXIS);
            }else if(kind.equals(StyleConstants.ComponentElementName)) {
                return new ComponentView(elem);
            }else if(kind.equals(StyleConstants.IconElementName)) {
                return new IconView(elem);
            }
        }
        return new LabelView(elem);
    }
}
class NoWrapEditorKit2 extends StyledEditorKit {
    public ViewFactory getViewFactory() {
        return new NoWrapViewFactory();
    }
}

// //Swing - Disabling word wrap for JTextPane
// //http://forums.sun.com/thread.jspa?threadID=622683
// class NoWrapEditorKit1 extends StyledEditorKit{
//     public ViewFactory getViewFactory() {
//         return new StyledViewFactory();
//     }
//     static class StyledViewFactory implements ViewFactory{
//         public View create(Element elem) {
//             String kind = elem.getName();
//             if(kind != null) {
//                 if(kind.equals(AbstractDocument.ContentElementName)) {
//                     return new LabelView(elem);
//                 }else if(kind.equals(AbstractDocument.ParagraphElementName)) {
//                     return new ParagraphView(elem);
//                 }else if(kind.equals(AbstractDocument.SectionElementName)) {
//                     return new NoWrapBoxView(elem, View.Y_AXIS);
//                 }else if(kind.equals(StyleConstants.ComponentElementName)) {
//                     return new ComponentView(elem);
//                 }else if(kind.equals(StyleConstants.IconElementName)) {
//                     return new IconView(elem);
//                 }
//             }
//             return new LabelView(elem);
//         }
//     }
//     static class NoWrapBoxView extends BoxView {
//         public NoWrapBoxView(Element elem, int axis) {
//             super(elem, axis);
//         }
//         public void layout(int width, int height) {
//             super.layout(Integer.MAX_VALUE-64, height);
//             //??? Integer.MAX_VALUE-64 = error?
//             //??? Integer.MAX_VALUE-64 = ok?
//         }
//     }
// }