/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;
import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author ASUS
 */
public class DBHelper {

    private static Connection connection;

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/db_badminton";
            String user = "root";
            String password = "";

            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Koneksi Berhasil");

        } catch (Exception e) {
            System.out.println("Koneksi Gagal");
            System.out.println(e);
        }

        return connection;
    }
}