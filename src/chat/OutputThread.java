/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author ngochuu
 */
public class OutputThread extends Thread {

    //socket
    private Socket socket;
    private JTextPane txtMessages;
    private BufferedReader br;
    private String sender, receiver;
    //Style
    private StyledDocument doc;
    private StyleContext context;
    private Style labelStyle;
    //emoji
    private final String[] emojies = {":happy:", ":neutral:", ":puzzled:", ":sad:", ":sleeping:", ":surprised:", ":vomited:", ":angry:", ":angel:"};

    public OutputThread(Socket chatSocket, JTextPane txtMessages, String sender, String receiver) {
        super();
        this.socket = chatSocket;
        this.txtMessages = txtMessages;
        this.sender = sender;
        this.receiver = receiver;

        context = new StyleContext();
        doc = new DefaultStyledDocument(context);
        labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);
        txtMessages.setDocument(doc);

        //connect socket
        try {
            br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Network error.");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (socket != null) {
                    String s;
                    while ((s = br.readLine()) != null) {
                        doc.insertString(doc.getLength(), receiver + ": " + s +"\n", null);
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something is wrong");
                e.printStackTrace();
                return;
            }
        }
    }
}
