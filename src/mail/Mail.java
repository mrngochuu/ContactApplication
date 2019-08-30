/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import util.MyUtils;

/**
 *
 * @author ngochuu
 */
public class Mail {

    private static final String HOST_NAME = "smtp.gmail.com";
    private static final int SSL_PORT = 465; //Port for SSL
    private static final int TSL_PORT = 587; //Port for TLS/STARTTLS
    private static final String APP_EMAIL = "ngochuu.bts@gmail.com";
    private static final String APP_PASSWORD = "NgocHuu215302577";

    public static void confirmMail(String confirmMail, String fullName) {
        // Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", HOST_NAME);
        props.put("mail.smtp.socketFactory.port", SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", SSL_PORT);

        // get Session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
            }
        });

        // compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(confirmMail));
            message.setSubject("Chat Application");
            message.setText("Dear " + fullName +",\n"
                    + "Welcome to chat application.\n"
                    + "We have received your registration request.\n"
                    + "Vui lòng nhập dãy số này:\n"
                    + "                " + MyUtils.getRandomNumber(100000, 999999) + "\n"
                            + "To complete the registration.\n"
                            + "                                Signature:\n"
                            + "                                Do Ngoc Huu");

            // send message
            Transport.send(message);

            System.out.println("Message sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        confirmMail("dongochuu95@gmail.com", "Do Ngoc Huu");
    }
}
