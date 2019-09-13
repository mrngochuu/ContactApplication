/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.awt.Dimension;
import static java.lang.Thread.sleep;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import util.MyUtils;

/**
 *
 * @author ngochuu
 */
public class EmojiThread extends Thread {

    private JTextPane txtMessages;
    private final String[] EMOJIES = {":happy:", ":neutral:", ":puzzled:", ":sad:", ":sleeping:", ":surprised:", ":vomited:", ":angry:", ":angel:"};
    private StyledDocument doc;
    private Style labelStyle;

    public EmojiThread(JTextPane txtMessages) {
        this.txtMessages = txtMessages;
        this.doc = txtMessages.getStyledDocument();
        StyleContext context = new StyleContext();
        labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);
    }

    private String getEmoji() {
        int length = doc.getLength();
        if (length > 0) {
            try {
                for (int i = 0; i < EMOJIES.length; i++) {
                    if (doc.getText(0, length).contains(EMOJIES[i])) {
                        return EMOJIES[i];
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String symbol = getEmoji();
                if (symbol != null) {
                    int pos = doc.getText(0, doc.getLength()).indexOf(symbol);
                    if (pos >= 0) {
                        JLabel label = new JLabel();
                        label.setPreferredSize(new Dimension(10, 10));
                        label.setIcon(MyUtils.resizePic("src/img/emoji/" + symbol.replace(":", "") + ".png", null, 10, 10));
                        StyleConstants.setComponent(labelStyle, label);
                        doc.remove(pos, symbol.length());
                        doc.insertString(pos, "emoji", labelStyle);
                    }
                }
                sleep(100);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
        }
    }
}
