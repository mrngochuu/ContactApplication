
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
public class ConnectionDB {

    public ConnectionDB() {
    }
    
    public static Connection connectDB(String driver, String url) {
        try {
            Connection con = null;
            Class.forName(driver);
            con = DriverManager.getConnection(url);
            return con;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
        return null;
    }
//    
//    public ResultSet executeQuery(String selectSql) {
//        if(con == null) {
//            return null; 
//        }
//        
//        try {
//            return stmt.executeQuery(selectSql);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//            e.printStackTrace();
//        }
//        return null;
//    }
//    
//    public int executeUpdate(String updateSql) {
//        if(con == null) return 0;
//        try {
//            return stmt.executeUpdate(updateSql);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//            e.printStackTrace();
//        }
//        return 0;
//    }
}
