/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interface.BookingInterface;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import Helper.DBBooking;
import Model.BookingModel;
import Model.LapanganModel;
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

    public String formatTanggal(
            Date tanggal
    ) {

        SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd"
                );
        return sdf.format(tanggal);
    }
    
    // validasi tanggal
    public boolean validasiTanggal(
            Calendar booking,
            Calendar hariIni
    ) {

        return !booking.before(hariIni);

    }
    
    public boolean validasiTanggalBooking(Date tanggalBooking) {

        Date hariIni = new Date();

        Calendar calHariIni = Calendar.getInstance();

        calHariIni.setTime(hariIni);

        calHariIni.set(Calendar.HOUR_OF_DAY,0);

        calHariIni.set(Calendar.MINUTE,0);

        calHariIni.set(Calendar.SECOND,0);

        calHariIni.set(Calendar.MILLISECOND,0);

        Calendar calBooking = Calendar.getInstance();

        calBooking.setTime(tanggalBooking);

        calBooking.set(Calendar.HOUR_OF_DAY,0);

        calBooking.set(Calendar.MINUTE,0);

        calBooking.set(Calendar.SECOND,0);

        calBooking.set(Calendar.MILLISECOND,0);

        return validasiTanggal(
                calBooking,
                calHariIni
        );
    }

    public String[] getJamMulai() {
        return new String[]{
            "07:00",
            "08:00",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00"
        };
    }
    
    public String[] getJamSelesai() {
        return new String[]{
            "08:00",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00",
            "22:00"
        };
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
    
    public boolean validasiJamRealtime(
        Date tanggalBooking,
        String jamMulai
    ) {

        Calendar calHariIni = Calendar.getInstance();

        calHariIni.setTime(new Date());

        Calendar calBooking = Calendar.getInstance();

        calBooking.setTime(tanggalBooking);

        int jamBooking = Integer.parseInt(jamMulai.substring(0, 2));

        int jamSekarang =  Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        return validasiJamBooking(
                calBooking,
                calHariIni,
                jamBooking,
                jamSekarang
        );
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
    public void tambahBooking(BookingModel booking) {
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
    
    public ArrayList<LapanganModel> getDataLapangan() {
        ArrayList<LapanganModel> list =
                new ArrayList<>();

        try {

            ResultSet rs =
                    DBLapangan.getLapangan();

            while (rs.next()) {

                LapanganModel lapangan =
                        new LapanganModel();

                lapangan.setIdLapangan(
                        rs.getInt("id_lapangan")
                );

                lapangan.setNamaLapangan(
                        rs.getString("nama_lapangan")
                );

                lapangan.setHargaPerJam(
                        rs.getInt("harga_per_jam")
                );

                lapangan.setStatus(
                        rs.getString("status")
                );

                list.add(lapangan);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }
    
    public int getHargaLapangan(String lapangan) {        
        return DBLapangan.getHargaLapangan(
                lapangan
        );
    }
        
    public String getStatusLapangan(String lapangan) {
        return DBLapangan.getStatusLapangan(
                lapangan
        );
    }
    
    public boolean cekBooking(int idLapangan, String tanggal, String jamMulai) {
        return DBBooking.cekBooking(idLapangan, tanggal, jamMulai);
    }
    
    public int hitungDurasi(String jamMulai, String jamSelesai) {
        int mulai = Integer.parseInt(jamMulai.substring(0, 2));
        int selesai = Integer.parseInt(jamSelesai.substring(0, 2));
        return selesai - mulai;
    }
    
    public BookingModel buatBooking(
        String nama,
        int idLapangan,
        String tanggal,
        String jamMulai,
        String jamSelesai,
        String namaLapangan
    ) {

        BookingModel booking = new BookingModel();

        booking.setNama(nama);
        booking.setIdLapangan(idLapangan);
        booking.setTanggal(tanggal);
        booking.setJamMulai(jamMulai);
        booking.setJamSelesai(jamSelesai);

        int durasi = hitungDurasi(jamMulai, jamSelesai);
        booking.setDurasi(durasi);

        int hargaPerJam =getHargaLapangan(namaLapangan);        
        int totalHarga = hitungTotal(durasi,hargaPerJam);

        booking.setTotalHarga(totalHarga);
        booking.setStatus("aktif");

        return booking;
    }
    
    public String prosesBooking(
            BookingModel booking,
            Date tanggalBooking,
            String namaLapangan
    ) throws BookingException {

        // validasi nama
        if (!validasiNama(booking.getNama())) {
            throw new BookingException(
                "Nama tidak boleh kosong!"
            );
        }
        
        String statusLapangan = getStatusLapangan(namaLapangan);
        // maintenance
        if (statusLapangan.equals("maintenance")) {
            throw new BookingException(
                "Lapangan sedang maintenance!"
            );
        }

        // tanggal lewat
        if (!validasiTanggalBooking(tanggalBooking)) {
            throw new BookingException(
                "Tidak bisa booking tanggal yang sudah lewat!"
            );
        }

        // maksimal H+3
        if (!validasiHPlus3(tanggalBooking)) {
            throw new BookingException(
                "Booking hanya bisa maksimal H+3!"
            );
        }

        // jam realtime
        if (!validasiJamRealtime(tanggalBooking,booking.getJamMulai())) {
            throw new BookingException(
                "Jam booking sudah lewat!"
            );
        }

        // bentrok
        if (cekBentrok(booking)) {
            throw new BookingException(
                "Jadwal sudah terisi!"
            );
        }

        // insert booking
        tambahBooking(booking);

        return "Booking berhasil!";
    }
    
    public String getDetailBooking(
        BookingModel booking,
        String namaLapangan
    ) {

        return "Konfirmasi Booking:\n\n"
                + "Lapangan : " + namaLapangan + "\n"
                + "Tanggal  : " + booking.getTanggal() + "\n"
                + "Jam      : "  + booking.getJamMulai() + " - " + booking.getJamSelesai() + "\n"
                + "Durasi   : " + booking.getDurasi() + " jam\n"
                + "Total    : Rp " + booking.getTotalHarga();
    }
    
    public Object[] buatRowJadwal(
        String waktu,
        ArrayList<Integer> listIdLapangan,
        ArrayList<String> listStatusLapangan,
        String tanggal,
        String jamMulai
    ) {

        Object[] row =
                new Object[
                        listIdLapangan.size() + 1
                ];

        row[0] = waktu;

        for (int i = 0; i < listIdLapangan.size(); i++) {

            int idLapangan =
                    listIdLapangan.get(i);

            String statusLap =
                    listStatusLapangan.get(i);

            String status =
                    getStatusJadwal(
                            idLapangan,
                            statusLap,
                            tanggal,
                            jamMulai
                    );

            row[i + 1] = status;
        }

        return row;
    }
    
    public String getStatusJadwal(
        int idLapangan,
        String statusLapangan,
        String tanggal,
        String jamMulai
    ) {

        // maintenance
        if (statusLapangan.equals("maintenance")) {
            return "Maintenance";
        }

        // booking
        if (cekBooking(idLapangan,tanggal,jamMulai)) {
            return "Terisi";
        }

        return "Tersedia";
    }
    
    public void updateStatusSelesai() {
        DBBooking.updateStatusSelesai();
    }
    
    public boolean validasiHPlus3(Date tanggalBooking) {

        Date hariIni = new Date();
        
        Calendar maxBooking = Calendar.getInstance();
        maxBooking.setTime(hariIni);
        maxBooking.add(Calendar.DAY_OF_MONTH,3);
        maxBooking.set(Calendar.HOUR_OF_DAY,0);
        maxBooking.set(Calendar.MINUTE,0);
        maxBooking.set(Calendar.SECOND,0);
        maxBooking.set(Calendar.MILLISECOND,0);

        Calendar calBooking =Calendar.getInstance();
        calBooking.setTime(tanggalBooking);
        calBooking.set(Calendar.HOUR_OF_DAY,0);
        calBooking.set(Calendar.MINUTE,0);
        calBooking.set(Calendar.SECOND,0);
        calBooking.set(Calendar.MILLISECOND,0);

        return !calBooking.after(
                maxBooking
        );
    }
}
