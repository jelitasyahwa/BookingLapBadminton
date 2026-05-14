/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Helper.DBAdmin;
/**
 *
 * @author ASUS
 */
public class AdminController {
     public boolean login(
            String username,
            String password
    ) {

        return DBAdmin.login(
                username,
                password
        );

    }

    public void register(
            String username,
            String password
    ) {

        DBAdmin.registerAdmin(
                username,
                password
        );

    }
    
    public boolean cekUsername(
            String username
    ) {

        return DBAdmin.cekUsername(
                username
        );
    }
}
