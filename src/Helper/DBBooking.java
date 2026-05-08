/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;
import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 *
 * @author ASUS
 */
public class DBBooking {
     public static void tambahBooking(
            String nama,
            int idLapangan,
            String tanggal,
            String jamMulai,
            String jamSelesai,
            int durasi,
            int totalHarga,
            String status
    ) {

        try {

            Connection conn = DBHelper.getConnection();

            String sql = "INSERT INTO booking "
                    + "(nama_pemesan, id_lapangan, tanggal, jam_mulai, jam_selesai, durasi, total_harga, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, nama);
            pst.setInt(2, idLapangan);
            pst.setString(3, tanggal);
            pst.setString(4, jamMulai);
            pst.setString(5, jamSelesai);
            pst.setInt(6, durasi);
            pst.setInt(7, totalHarga);
            pst.setString(8, status);

            pst.executeUpdate();

            System.out.println("Booking berhasil");

        } catch (Exception e) {

            System.out.println("Booking gagal");
            System.out.println(e);

        }
    }
    
}
