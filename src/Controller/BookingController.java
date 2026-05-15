/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interface.BookingInterface;
import java.util.Calendar;
import Helper.DBBooking;
import Model.BookingModel;
import Exception.BookingException;
import Helper.DBLapangan;

/**
 *
 * @author ASUS
 */
public class BookingController implements BookingInterface {
    public boolean validasiNama(String nama) {
        return !nama.trim().isEmpty();
    }

    // validasi tanggal
    public boolean validasiTanggal(
            Calendar booking,
            Calendar hariIni
    ) {

        return !booking.before(hariIni);

    }

    // validasi maksimal booking
    public boolean validasiMaksimalBooking(
            Calendar booking,
            Calendar maksimal
    ) {

        return !booking.after(maksimal);

    }

    // validasi jam booking
    public boolean validasiJamBooking(
            Calendar booking,
            Calendar hariIni,
            int jamBooking,
            int jamSekarang
    ) {

        // jika booking untuk hari ini
        if (
                booking.get(Calendar.YEAR)
                == hariIni.get(Calendar.YEAR)
                &&
                booking.get(Calendar.DAY_OF_YEAR)
                == hariIni.get(Calendar.DAY_OF_YEAR)
                &&
                jamBooking < jamSekarang
        ) {

            return false;
        }

        return true;
    }

    @Override
    // hitung total harga
    public int hitungTotal(
            int durasi,
            int hargaPerJam
    ) {

        return durasi * hargaPerJam;

    }

    // cek bentrok
    public boolean cekBentrok(
            BookingModel booking
    ) {

        return DBBooking.cekBentrok(

                booking.getIdLapangan(),

                booking.getTanggal(),

                booking.getJamMulai(),

                booking.getJamSelesai()
        );
    }

    // tambah booking
    public void tambahBooking(
            BookingModel booking
    ) {

        DBBooking.tambahBooking(

                booking.getNama(),

                booking.getIdLapangan(),

                booking.getTanggal(),

                booking.getJamMulai(),

                booking.getJamSelesai(),

                booking.getDurasi(),

                booking.getTotalHarga(),

                booking.getStatus()
        );
    }

    // proses booking
    public String booking(
            BookingModel booking
    ) throws BookingException {

        // validasi nama
        if (!validasiNama(
                booking.getNama()
        )) {

            throw new BookingException(
                    "Nama tidak boleh kosong!"
            );
        }

        // cek bentrok
        boolean bentrok =
                cekBentrok(booking);

        if (bentrok) {

            throw new BookingException(
                    "Jadwal sudah terisi!"
            );
        }

        // insert booking
        tambahBooking(booking);

        return "Booking berhasil!";
    }
    
    public int getHargaLapangan(String lapangan) {

    return DBLapangan.getHargaLapangan(
            lapangan
    );
}
}
