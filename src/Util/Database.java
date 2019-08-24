package Util;

import java.sql.*;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ngochuu
 */
public class Database {

    private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String url = "jdbc:sqlserver://192.168.0.10:1433; databasename=ContactDB; username=sa; password=NgocHuu215302577";
    private static final String defaultPathImg = "src/img/Unknown.png";

    public Database() {
    }

    public static Connection connectDB() throws Exception {
        Connection con = null;
        Class.forName(driver);
        con = DriverManager.getConnection(url);
        return con;
    }

    public static int getUserId(String userName) {
        try {
            Connection con = Database.connectDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select id From tblUsers where userName='" + userName + "'");
            if (!rs.next()) {
                return -1;
            } else {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int addNewUser(String firstName, String lastName, String userName, String password, String email) throws Exception {
        Connection con = Database.connectDB();
        //set default Group and picture
        PreparedStatement ps = con.prepareStatement("INSERT INTO tblusers (firstName,lastName,userName,password,email,[group],picture) values (?,?,?,?,?,?,?)");
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, userName);
        ps.setString(4, MyUtils.generateHash(password));
        ps.setString(5, email);
        ps.setInt(6, 0);
        ps.setBytes(7, MyUtils.getImgbinary(defaultPathImg));
        return ps.executeUpdate();
    }

    public static boolean checkLogin(String userName, String password) throws Exception {
        Connection con = connectDB();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM tblUsers WHERE username = ? AND password = ?");
        ps.setString(1, userName);
        ps.setString(2, MyUtils.generateHash(password));
        return ps.execute();
    }

    public static ResultSet getRecordByUserID(int userId) throws Exception {
        Connection con = Database.connectDB();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("Select * FROM tblUsers WHERE id=" + userId);
        return rs;
    }

    public static int updateInfoUser(int userID, String firstName, String lastName, String phone, int group, String pathImg, String address) throws Exception {
        Connection con = connectDB();
        String sql = "UPDATE tblUsers SET firstName = '" + firstName + "', lastName = '" + lastName + "'";
        if (!phone.isEmpty()) {
            sql += ",phone = '" + phone + "'";
        } else {
            sql += ", phone = " + null;
        }
        sql += ", [group] = " + group + "";
        if (!pathImg.isEmpty() && pathImg != null) {
            sql += ", picture = ?";
        }

        if (!address.isEmpty()) {
            sql += ", address = '" + address + "'";
        } else {
            sql += ", address = " + null;
        }

        sql += " where id = " + userID;

        PreparedStatement ps = con.prepareStatement(sql);
        if (!pathImg.isEmpty() && pathImg != null) {
            ps.setBytes(1, MyUtils.getImgbinary(pathImg));
        }
        return ps.executeUpdate();
    }
}
