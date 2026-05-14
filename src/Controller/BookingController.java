/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interface.BookingInterface;
import java.util.Calendar;

/**
 *
 * @author ASUS
 */
public class BookingController implements BookingInterface {
    // validasi nama
    public boolean validasiNama(String nama) {
        return !nama.trim().isEmpty();
    }
    
    @Override
    // method hitung total harga
    public int hitungTotal(
            int durasi,
            int hargaPerJam
    ) {
        return durasi * hargaPerJam;
    }

    public boolean validasiTanggal(
            Calendar booking,
            Calendar hariIni
    ) {

        return !booking.before(hariIni);

    }
}
