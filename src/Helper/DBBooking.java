/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


/**
 *
 * @author ASUS
 */
public class DBBooking {
    
    // method tambah booking
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
    
    // method cek booking
    public static boolean cekBooking(
        int idLapangan,
        String tanggal,
        String jamCek
    ) {

        boolean terisi = false;

        try {
            Connection conn = DBHelper.getConnection();
            String sql =
        "SELECT * FROM booking "
        + "WHERE id_lapangan=? "
        + "AND tanggal=? "
        + "AND status='aktif' "
        + "AND jam_mulai <= ? "
        + "AND jam_selesai > ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, idLapangan);
            pst.setString(2, tanggal);
            pst.setString(3, jamCek);
            pst.setString(4, jamCek);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                terisi = true;
            }

        } catch (Exception e) {
            System.out.println(e);
        } 
        return terisi;
    }
    
    // method cek jam bentrok
    public static boolean cekBentrok(
        int idLapangan,
        String tanggal,
        String jamMulai,
        String jamSelesai
    ) {

    boolean bentrok = false;

    try {
        Connection conn = DBHelper.getConnection();
        String sql =
                "SELECT * FROM booking "
                + "WHERE id_lapangan=? "
                + "AND tanggal=? "
                + "AND ("
                + "(jam_mulai < ? AND jam_selesai > ?)"
                + ")";

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, idLapangan);
        pst.setString(2, tanggal);
        pst.setString(3, jamSelesai);
        pst.setString(4, jamMulai);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            bentrok = true;
        }

    } catch (Exception e) {
        System.out.println(e);
    }

    return bentrok;
    
    }
    
    public static void updateStatusSelesai() {

        try {

            Connection conn =
                    DBHelper.getConnection();

            String sql =
                    "UPDATE booking "
                    + "SET status='selesai' "
                    + "WHERE status='aktif' "
                    + "AND CONCAT(tanggal, ' ', jam_selesai) < NOW()";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.executeUpdate();

        } catch (Exception e) {

            System.out.println(e);

        }
    }


    
    public static ResultSet getBooking() {

    ResultSet rs = null;

    try {

        Connection conn =
                DBHelper.getConnection();

        String sql =
                "SELECT booking.*, lapangan.nama_lapangan "
                + "FROM booking "
                + "JOIN lapangan "
                + "ON booking.id_lapangan = lapangan.id_lapangan";

        PreparedStatement pst =
                conn.prepareStatement(sql);

        rs = pst.executeQuery();

    } catch (Exception e) {

        System.out.println(e);

    }

    return rs;

}
}