/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ASUS
 */
public class DBLapangan {
     public static ResultSet getLapangan() {

        ResultSet rs = null;

        try {

            Connection conn = DBHelper.getConnection();

            Statement st = conn.createStatement();

            String sql = "SELECT * FROM lapangan WHERE status='tersedia'";

            rs = st.executeQuery(sql);

        } catch (Exception e) {

            System.out.println("Gagal mengambil data lapangan");
            System.out.println(e);

        }

        return rs;
    }
    
}
