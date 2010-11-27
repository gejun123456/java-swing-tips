package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        super(new BorderLayout());
        JTextPane textPane = new JTextPane(new SimpleSyntaxDocument());
        textPane.setText("red green, blue. red-green;bleu.");
        add(new JScrollPane(textPane));
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
//From: http://www.discoverteenergy.com/files/SyntaxDocument.java
class SimpleSyntaxDocument extends DefaultStyledDocument {
    HashMap<String,AttributeSet> keywords = new HashMap<String,AttributeSet>();
    MutableAttributeSet normal = new SimpleAttributeSet();
    public SimpleSyntaxDocument() {
        super();
        StyleConstants.setForeground(normal, Color.BLACK);
        MutableAttributeSet color;
        StyleConstants.setForeground(color = new SimpleAttributeSet(), Color.RED);
        keywords.put("red", color);
        StyleConstants.setForeground(color = new SimpleAttributeSet(), Color.GREEN);
        keywords.put("green", color);
        StyleConstants.setForeground(color = new SimpleAttributeSet(), Color.BLUE);
        keywords.put("blue", color);
    }
    @Override public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        processChangedLines(offset, str.length());
    }
    @Override public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);
        processChangedLines(offset, 0);
    }
    private void processChangedLines(int offset, int length) throws BadLocationException {
        Element root = getDefaultRootElement();
        String content = getText(0, getLength());
        int startLine = root.getElementIndex( offset );
        int endLine = root.getElementIndex( offset + length );
        for (int i = startLine; i <= endLine; i++) {
            applyHighlighting(content, i);
        }
    }
    private void applyHighlighting(String content, int line) throws BadLocationException {
        Element root = getDefaultRootElement();
        int startOffset   = root.getElement( line ).getStartOffset();
        int endOffset     = root.getElement( line ).getEndOffset() - 1;
        int lineLength    = endOffset - startOffset;
        int contentLength = content.length();
        if (endOffset >= contentLength) endOffset = contentLength - 1;
        setCharacterAttributes(startOffset, lineLength, normal, true);
        checkForTokens(content, startOffset, endOffset);
    }
    private void checkForTokens(String content, int startOffset, int endOffset) {
        while (startOffset <= endOffset) {
            while (isDelimiter(content.substring(startOffset, startOffset+1))) {
                if (startOffset < endOffset) {
                    startOffset++;
                } else {
                    return;
                }
            }
            startOffset = getOtherToken(content, startOffset, endOffset);
        }
    }
    private int getOtherToken(String content, int startOffset, int endOffset) {
        int endOfToken = startOffset + 1;
        while ( endOfToken <= endOffset ) {
            if ( isDelimiter( content.substring(endOfToken, endOfToken + 1) ) ) {
                break;
            }
            endOfToken++;
        }
        String token = content.substring(startOffset, endOfToken);
        if ( keywords.containsKey( token ) ) {
            setCharacterAttributes(startOffset, endOfToken - startOffset, keywords.get(token), false);
        }
        return endOfToken + 1;
    }
    String operands = ".,";
    protected boolean isDelimiter(String character) {
        return Character.isWhitespace(character.charAt(0)) || operands.indexOf(character)!=-1;
    }
}