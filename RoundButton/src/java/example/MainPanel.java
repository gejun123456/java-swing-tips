package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

class MainPanel extends JPanel{
    private final JButton button = new JButton("RoundedCornerButtonUI");
    public MainPanel() {
        super();
        add(new JButton("Default JButton"));
        button.setUI(new RoundedCornerButtonUI());
        add(button);
        add(new RoundedCornerButton("Rounded Corner Button"));
        add(new RoundButton(new ImageIcon(getClass().getResource("16x16.png"))) {
            @Override public Dimension getPreferredSize() {
                int r = 16 + ( focusstroke + 4 ) * 2; //test margin = 4
                return new Dimension(r, r);
            }
        });
        add(new ShapeButton(makeStar(25,30,20)));
        add(new RoundButton("Round Button"));
        setPreferredSize(new Dimension(320, 200));
    }
    private Path2D.Double makeStar(int r1, int r2, int vc) {
        int or = Math.max(r1, r2);
        int ir = Math.min(r1, r2);
        double agl = 0.0;
        double add = 2*Math.PI/(vc*2);
        Path2D.Double p = new Path2D.Double();
        p.moveTo(or*1, or*0);
        for(int i=0;i<vc*2-1;i++) {
            agl+=add;
            int r = i%2==0?ir:or;
            p.lineTo(r*Math.cos(agl), r*Math.sin(agl));
        }
        p.closePath();
        AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2,or,0);
        return new Path2D.Double(p, at);
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

class RoundedCornerButton extends JButton {
    private static final float arcwidth  = 16.0f;
    private static final float archeight = 16.0f;
    protected static final int focusstroke = 2;
    protected final Color fc = new Color(100,150,255,200);
    protected final Color ac = new Color(230,230,230);
    protected final Color rc = Color.ORANGE;
    protected Shape shape;
    protected Shape border;
    protected Shape base;
    public RoundedCornerButton() {
        this(null, null);
    }
    public RoundedCornerButton(Icon icon) {
        this(null, icon);
    }
    public RoundedCornerButton(String text) {
        this(text, null);
    }
    public RoundedCornerButton(Action a) {
        this();
        setAction(a);
    }
    public RoundedCornerButton(String text, Icon icon) {
        setModel(new DefaultButtonModel());
        init(text, icon);
        setContentAreaFilled(false);
        setBackground(new Color(250, 250, 250));
        initShape();
    }

    protected void initShape() {
        if(!getBounds().equals(base)) {
            base = getBounds();
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arcwidth, archeight);
            border = new RoundRectangle2D.Float(focusstroke, focusstroke,
                                                getWidth()-1-focusstroke*2,
                                                getHeight()-1-focusstroke*2,
                                                arcwidth, archeight);
        }
    }
    private void paintFocusAndRollover(Graphics2D g2, Color color) {
        g2.setPaint(new GradientPaint(0, 0, color, getWidth()-1, getHeight()-1, color.brighter(), true));
        g2.fill(shape);
        g2.setColor(getBackground());
        g2.fill(border);
    }

    @Override protected void paintComponent(Graphics g) {
        initShape();
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(getModel().isArmed()) {
            g2.setColor(ac);
            g2.fill(shape);
        }else if(isRolloverEnabled() && getModel().isRollover()) {
            paintFocusAndRollover(g2, rc);
        }else if(hasFocus()) {
            paintFocusAndRollover(g2, fc);
        }else{
            g2.setColor(getBackground());
            g2.fill(shape);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setColor(getBackground());
        super.paintComponent(g2);
    }
    @Override protected void paintBorder(Graphics g) {
        initShape();
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.draw(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    @Override public boolean contains(int x, int y) {
        initShape();
        return shape.contains(x, y);
    }
}

class RoundButton extends RoundedCornerButton {
    public RoundButton() {
        this(null, null);
    }
    public RoundButton(Icon icon) {
        this(null, icon);
    }
    public RoundButton(String text) {
        this(text, null);
    }
    public RoundButton(Action a) {
        this();
        setAction(a);
    }
    public RoundButton(String text, Icon icon) {
        setModel(new DefaultButtonModel());
        init(text, icon);
        setFocusPainted(false);
        initShape();
    }
    @Override public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.width = d.height = Math.max(d.width, d.height);
        return d;
    }
    @Override protected void initShape() {
        if(!getBounds().equals(base)) {
            base = getBounds();
            shape = new Ellipse2D.Float(0, 0, getWidth()-1, getHeight()-1);
            border = new Ellipse2D.Float(focusstroke, focusstroke,
                                         getWidth()-1-focusstroke*2,
                                         getHeight()-1-focusstroke*2);
        }
    }
}

class RoundedCornerButtonUI extends BasicButtonUI{
    private static final float arcwidth  = 16.0f;
    private static final float archeight = 16.0f;
    protected static final int focusstroke = 2;
    protected final Color fc = new Color(100,150,255,200);
    protected final Color ac = new Color(230,230,230);
    protected final Color rc = Color.ORANGE;
    protected Shape shape;
    protected Shape border;
    protected Shape base;

    @Override protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setBackground(new Color(250, 250, 250));
        initShape(b);
    }
    @Override protected void installListeners(AbstractButton b) {
        BasicButtonListener listener = new BasicButtonListener(b) {
            @Override public void mousePressed(MouseEvent e) {
                AbstractButton b = (AbstractButton) e.getSource();
                initShape(b);
                if(shape.contains(e.getX(), e.getY())) {
                    super.mousePressed(e);
                }
            }
            @Override public void mouseEntered(MouseEvent e) {
                if(shape.contains(e.getX(), e.getY())) {
                    super.mouseEntered(e);
                }
            }
            @Override public void mouseMoved(MouseEvent e) {
                if(shape.contains(e.getX(), e.getY())) {
                    super.mouseEntered(e);
                }else{
                    super.mouseExited(e);
                }
            }
        };
        if(listener != null) {
            b.addMouseListener(listener);
            b.addMouseMotionListener(listener);
            b.addFocusListener(listener);
            b.addPropertyChangeListener(listener);
            b.addChangeListener(listener);
        }
    }
    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D)g;
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        initShape(b);

        //ContentArea
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(model.isArmed()) {
            g2.setColor(ac);
            g2.fill(shape);
        }else if(b.isRolloverEnabled() && model.isRollover()) {
            paintFocusAndRollover(g2, c, rc);
        }else if(b.hasFocus()) {
            paintFocusAndRollover(g2, c, fc);
        }else{
            g2.setColor(c.getBackground());
            g2.fill(shape);
        }
        //Border
        g2.setPaint(c.getForeground());
        g2.draw(shape);

        g2.setColor(c.getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        super.paint(g2, c);
    }
    private void initShape(JComponent c) {
        if(!c.getBounds().equals(base)) {
            base = c.getBounds();
            shape = new RoundRectangle2D.Float(0, 0, c.getWidth()-1, c.getHeight()-1, arcwidth, archeight);
            border = new RoundRectangle2D.Float(focusstroke, focusstroke,
                                                c.getWidth()-1-focusstroke*2,
                                                c.getHeight()-1-focusstroke*2,
                                                arcwidth, archeight);
        }
    }
    private void paintFocusAndRollover(Graphics2D g2, JComponent c, Color color) {
        g2.setPaint(new GradientPaint(0, 0, color, c.getWidth()-1, c.getHeight()-1, color.brighter(), true));
        g2.fill(shape);
        g2.setColor(c.getBackground());
        g2.fill(border);
    }
}

class ShapeButton extends JButton {
    protected final Color fc = new Color(100,150,255,200);
    protected final Color ac = new Color(230,230,230);
    protected final Color rc = Color.ORANGE;
    protected Shape shape;
    public ShapeButton(Shape s) {
        shape = s;
        setModel(new DefaultButtonModel());
        init("Shape", new DummySizeIcon(s));
        setVerticalAlignment(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBackground(new Color(250, 250, 250));
    }
    private void paintFocusAndRollover(Graphics2D g2, Color color) {
        g2.setPaint(new GradientPaint(0, 0, color, getWidth()-1, getHeight()-1, color.brighter(), true));
        g2.fill(shape);
    }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(getModel().isArmed()) {
            g2.setColor(ac);
            g2.fill(shape);
        }else if(isRolloverEnabled() && getModel().isRollover()) {
            paintFocusAndRollover(g2, rc);
        }else if(hasFocus()) {
            paintFocusAndRollover(g2, fc);
        }else{
            g2.setColor(getBackground());
            g2.fill(shape);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setColor(getBackground());
        super.paintComponent(g2);
    }
    @Override protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.draw(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    @Override public boolean contains(int x, int y) {
        return shape.contains(x, y);
    }
    private static class DummySizeIcon implements Icon{
        private final Shape shape;
        public DummySizeIcon(Shape s) {
            shape = s;
        }
        @Override public int getIconWidth() {
            return shape.getBounds().width;
        }
        @Override public int getIconHeight() {
            return shape.getBounds().height;
        }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {}
    }
}