/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Helper.DBLapangan;

/**
 *
 * @author ASUS
 */
public class LapanganController {
       public void tambahLapangan(
            String nama,
            int harga,
            String status
    ) {

        DBLapangan.tambahLapangan(
                nama,
                harga,
                status
        );

    }

    public void editLapangan(
            int id,
            String nama,
            int harga,
            String status
    ) {

        DBLapangan.editLapangan(
                id,
                nama,
                harga,
                status
        );

    }

    public void hapusLapangan(
            int id
    ) {

        DBLapangan.hapusLapangan(id);

    }
    
}
