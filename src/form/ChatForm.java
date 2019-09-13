/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import chat.ChatPanel;
import database.Database;
import java.awt.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import util.MyUtils;

/**
 *
 * @author ngochuu
 */
public class ChatForm extends javax.swing.JFrame {

    private int userID;
    private int userPort;
    private String userIP;
    private String userName;
    //server
    private ServerSocket srvSocket = null;
    private BufferedReader srvBr = null;
    private DataOutputStream srvDos = null;
    //the other connect
    private Socket cliSocket = null;
    private String clientName;

    // connect the other
    private Socket otherSocket = null;
    private BufferedReader otherBr = null;
    private DataOutputStream otherDos = null;
    
    // username others

    /**
     * Creates new form ChatForm
     */
    public ChatForm(int userID, String username) {
        initComponents();
        this.userID = userID;
        this.userName = username;
        lblUserName.setText(username);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        try {
            //random port and open server socket
            userPort = createPort();
            srvSocket = new ServerSocket(userPort);
            userIP = Inet4Address.getLocalHost().getHostAddress();
            //save server's infomation into db
            Database.executeUpdate("INSERT INTO tblSocket (userID, socketNum, socketIP) values (" + this.userID + "," + userPort + ",'" + userIP + "')");
            //setAvatar
            ResultSet rs = Database.executeQuery("SELECT * FROM tblUsers WHERE id = " + this.userID);
            if (rs.next()) {
                lblAvatar.setIcon(MyUtils.resizePic(null, rs.getBytes("picture"), lblAvatar.getWidth(), lblAvatar.getHeight()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }

        //accept another sockets to connect
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        cliSocket = srvSocket.accept();
                        if (cliSocket != null) {
                            srvBr = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
                            srvDos = new DataOutputStream(cliSocket.getOutputStream());
                            //get Client name
                            int otherID = srvBr.read();
                            String clientName = Database.getUsernameByID(otherID);
                            ChatPanel p = new ChatPanel(cliSocket, userName, clientName);
                            tbpChat.add(clientName, p);
                            p.updateUI();
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //update online username into list friends
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DefaultListModel model = new DefaultListModel();
                        ResultSet rs = Database.executeQuery("SELECT username FROM tblusers WHERE ID IN (SELECT userID FROM tblSocket)");
                        while (rs.next()) {
                            if (!rs.getString(1).equals(userName)) {
                                model.addElement(rs.getString(1));
                            }
                        }
                        listFriends.setModel(model);
                        rs.close();
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //Close the window and delete socket in database
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Database.executeUpdate("DELETE FROM tblSocket WHERE userID = " + userID);
                    System.exit(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                    ex.printStackTrace();
                }
            }
        });
    }

    private int createPort() throws Exception {
        int port;
        ResultSet rs;
        do {
            port = randomNum();
            rs = Database.executeQuery("Select * From tblSocket where socketNum = " + port);
        } while (rs.next());
        return port;
    }

    private int randomNum() {
        Random rd = new Random();
        return rd.nextInt(65535 - 3000) + 3000;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblAvatar = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tbpChat = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFriends = new javax.swing.JList<>();
        btnAccount = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        lblUserName.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(255, 255, 255));
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUserName.setText("Username");

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CHAT APPLICATION");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108)
                .addComponent(jLabel5)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUserName)
                            .addComponent(lblAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "List friends", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 15), new java.awt.Color(0, 102, 255))); // NOI18N

        listFriends.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listFriends.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listFriendsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listFriends);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1))
        );

        btnAccount.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        btnAccount.setText("Your account");
        btnAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tbpChat, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tbpChat, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(18, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountActionPerformed
        InfomationForm infomationForm = new InfomationForm(userID);
        infomationForm.setVisible(true);
        infomationForm.pack();
        infomationForm.setLocationRelativeTo(null);
        infomationForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_btnAccountActionPerformed

    private void listFriendsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listFriendsMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            //handle double click event.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //get IP and port
                        String serverName = listFriends.getModel().getElementAt(listFriends.getSelectedIndex());
                        ResultSet rs = Database.executeQuery("SELECT * FROM tblSocket WHERE userID IN (SELECT id from tblusers where username = '" + serverName + "')");
                        if (rs.next()) {
                            String otherIP = rs.getString("SocketIP");
                            int otherPort = rs.getInt("socketNum");
                            otherSocket = new Socket(otherIP, otherPort);
                            if (otherSocket != null) {
                                otherBr = new BufferedReader(new InputStreamReader(otherSocket.getInputStream()));
                                otherDos = new DataOutputStream(otherSocket.getOutputStream());
                                otherDos.write(userID);
                                otherDos.write(13);
                                otherDos.flush();
                                //Create chat screen
                                ChatPanel p = new ChatPanel(otherSocket, userName, serverName);
                                tbpChat.add(serverName, p);
                                p.updateUI();
                            }
                        }
                        rs.close();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }//GEN-LAST:event_listFriendsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccount;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JList<String> listFriends;
    private javax.swing.JTabbedPane tbpChat;
    // End of variables declaration//GEN-END:variables
}
