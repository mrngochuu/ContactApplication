package database;

import util.MyUtils;
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

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://192.168.0.10:1433; databasename=ContactDB; username=sa; password=NgocHuu215302577";
    private static final String DEFAULT_PATH_IMG = "src/img/avatar/Unknown.png";

    public Database() {
    }

    public static Connection connectDB() throws Exception {
        Connection con = null;
        Class.forName(DRIVER);
        con = DriverManager.getConnection(URL);
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
        ps.setBytes(7, MyUtils.getImgbinary(DEFAULT_PATH_IMG));
        return ps.executeUpdate();
    }

    public static boolean checkLogin(String userName, String password) throws Exception {
        Connection con = connectDB();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM tblusers WHERE username = ? AND [password] = ?");
        ps.setString(1, userName);
        ps.setString(2, MyUtils.generateHash(password));
        ResultSet rs = ps.executeQuery();
        return rs.next();
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

    public static ResultSet executeQuery(String sql) throws Exception {
        Connection con = connectDB();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        return rs;
    }

    public static int executeUpdate(String sql) throws Exception {
        Connection con = connectDB();
        Statement st = con.createStatement();
        return st.executeUpdate(sql);
    }

    public static String getUsernameByID(int userID) throws Exception {
        Connection con = Database.connectDB();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("Select userName FROM tblusers WHERE id = " + userID);
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }
}