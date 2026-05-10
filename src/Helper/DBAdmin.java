/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author ASUS
 */
public class DBAdmin {
    
    public static boolean login(
            String username,
            String password
    ) {

        boolean berhasil = false;

        try {

            Connection conn = DBHelper.getConnection();
            
            String sql =
                    "SELECT * FROM admin "
                    + "WHERE username=? AND password=?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                berhasil = true;
            }

        } catch (Exception e) {
             System.out.println(e);
        }
        
        return berhasil;
    }
    


    
}
