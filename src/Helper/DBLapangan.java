/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
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

            String sql =
                    "SELECT * FROM lapangan";

            rs = st.executeQuery(sql);

        } catch (Exception e) {

            System.out.println("Gagal mengambil data lapangan");
            System.out.println(e);

        }

        return rs;
    }
     
    public static int getHargaLapangan(String namaLapangan) {

        int harga = 0;

        try {

            String sql =
                "SELECT harga_per_jam FROM lapangan WHERE nama_lapangan=?";

            PreparedStatement ps =
                DBHelper.getConnection().prepareStatement(sql);

            ps.setString(1, namaLapangan);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                harga = rs.getInt("harga_per_jam");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return harga;
    }
     
    public static void tambahLapangan(
        String nama,
        int harga,
        String status
) {

    try {

        Connection conn =
                DBHelper.getConnection();

        System.out.println("KONEK DB");

        String sql =
                "INSERT INTO lapangan "
                + "(nama_lapangan, harga_per_jam, status) "
                + "VALUES (?, ?, ?)";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setString(1, nama);
        ps.setInt(2, harga);
        ps.setString(3, status);

        ps.executeUpdate();

        System.out.println("INSERT BERHASIL");

    } catch (Exception e) {

        System.out.println("ERROR INSERT");
        System.out.println(e);

    }

}
     public static void editLapangan(
        int id,
        String nama,
        int harga,
        String status
) {

    try {

        Connection conn =
                DBHelper.getConnection();

        String sql =
                "UPDATE lapangan SET "
                + "nama_lapangan=?, "
                + "harga_per_jam=?, "
                + "status=? "
                + "WHERE id_lapangan=?";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setString(1, nama);
        ps.setInt(2, harga);
        ps.setString(3, status);
        ps.setInt(4, id);

        ps.executeUpdate();

    } catch (Exception e) {

        System.out.println(e);

    }

}
    public static void hapusLapangan(
        int id
    ) {

    try {

        Connection conn =
                DBHelper.getConnection();

        String sql =
                "DELETE FROM lapangan "
                + "WHERE id_lapangan=?";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(1, id);

        ps.executeUpdate();

    } catch (Exception e) {

        System.out.println(e);

    }

}
    public static String getStatusLapangan(String namaLapangan) {

    String status = "";

    try {

        String sql =
            "SELECT status FROM lapangan WHERE nama_lapangan=?";

        PreparedStatement ps =
            DBHelper.getConnection().prepareStatement(sql);

        ps.setString(1, namaLapangan);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            status = rs.getString("status");
        }

    } catch (Exception e) {
        System.out.println(e);
    }

    return status;
}
    // status lapangan di user
    public static ResultSet cariLapangan(
        String keyword
) {

    ResultSet rs = null;

    try {

        Connection conn =
                DBHelper.getConnection();

            String sql =
        "SELECT * FROM lapangan "
        + "WHERE nama_lapangan LIKE ?";
            
        PreparedStatement pst =
                conn.prepareStatement(sql);

        pst.setString(
                1,
              "% " + keyword + "%"        );

        rs = pst.executeQuery();

    } catch (Exception e) {

        System.out.println(e);
    }

    return rs;
}
    
}


