/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;
import Helper.DBLapangan;
import java.sql.ResultSet;
import Helper.DBBooking;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class UserBookingView extends javax.swing.JFrame {
    
    private ArrayList<Integer> listIdLapangan = new ArrayList<>();

    /**
     * Creates new form UserBookingView
     */
    public UserBookingView() {
        initComponents();
        
        loadJamMulai();
        loadJamSelesai(0);
        loadLapangan();
        loadJadwal();
        
    }
    
    // method jam mulai
    private void loadJamMulai() {

        cbJamMulai.removeAllItems();

        String[] jam = {
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

        for (String j : jam) {
            cbJamMulai.addItem(j);
        }
    }
    
    // method jam selesai
    private void loadJamSelesai(int mulaiIndex) {

        cbJamSelesai.removeAllItems();

        String[] jam = {
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

        for (int i = mulaiIndex; i < jam.length; i++) {
            cbJamSelesai.addItem(jam[i]);
        }
    }
    
    // method lapangan
    private void loadLapangan() {

        try {

            cbLapangan.removeAllItems();

            ResultSet rs = DBLapangan.getLapangan();

            while (rs.next()) {

                cbLapangan.addItem(rs.getString("nama_lapangan"));

            }

        } catch (Exception e) {

            System.out.println("Gagal load lapangan");
            System.out.println(e);

        }
    }
    
    // method jadwal
    private void loadJadwal() {

        DefaultTableModel model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
        };

        model.addColumn("Jam");

        listIdLapangan.clear();

        try {

            ResultSet rsLapangan =
                    DBLapangan.getLapangan();

            ArrayList<String> listNamaLapangan = new ArrayList<>();
            ArrayList<String> listStatusLapangan = new ArrayList<>();

            while (rsLapangan.next()) {

                model.addColumn(
                        rsLapangan.getString(
                                "nama_lapangan"
                        )
                );

                listIdLapangan.add(
                        rsLapangan.getInt(
                                "id_lapangan"
                        )
                );

                listNamaLapangan.add(
                        rsLapangan.getString(
                                "nama_lapangan"
                        )
                );

                listStatusLapangan.add(
                        rsLapangan.getString(
                                "status"
                        )
                );
            }

            for (int jam = 7; jam < 22; jam++) {

                String jamMulai = String.format("%02d:00", jam);
                String jamSelesai = String.format("%02d:00", jam + 1);

                String waktu = jamMulai + " - " + jamSelesai;
                String tanggal = "";

                if (jDateChooser1.getDate() != null) {
                    SimpleDateFormat sdf =
                        new SimpleDateFormat(
                            "yyyy-MM-dd"
                        );
                    tanggal = sdf.format(
                        jDateChooser1.getDate()
                    );
                }

                Object[] row = new Object[
                    listIdLapangan.size() + 1
                ];

                row[0] = waktu;

                for (int i = 0; i < listIdLapangan.size(); i++) {

                    int idLapangan = listIdLapangan.get(i);

                    String statusLap = listStatusLapangan.get(i);
                    String status = "Tersedia";

                    // cek maintenance
                    if ( statusLap.equals(
                            "maintenance"
                            )
                        ) {
                            status = "Maintenance";
                        }

                    // cek booking
                    else if ( DBBooking.cekBooking(
                                idLapangan,
                                tanggal,
                                jamMulai
                                )
                            ) {
                                status = "Terisi";
                            }
                            row[i + 1] = status;
                        }
                        model.addRow(row);
                    }
                    tableJadwal.setModel(model);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // method total harga
    private void hitungTotal() {

        try {
            
            if (cbJamMulai.getSelectedItem() == null ||
                cbJamSelesai.getSelectedItem() == null) {
                return;
            }
            
            String jamMulai = cbJamMulai.getSelectedItem().toString();
            String jamSelesai = cbJamSelesai.getSelectedItem().toString();

            int mulai = Integer.parseInt(jamMulai.substring(0, 2));
            int selesai = Integer.parseInt(jamSelesai.substring(0, 2));

            int durasi = selesai - mulai;

            String lapangan = cbLapangan.getSelectedItem().toString();

            int hargaPerJam =
                DBLapangan.getHargaLapangan(lapangan);

            int total = durasi * hargaPerJam;

            // tampilkan ke label
            lblHargaPerJam.setText("Rp " + hargaPerJam);

            lblDurasi.setText(durasi + " jam");

            lblTotalHarga.setText("Rp " + total);

        } catch (Exception e) {

            System.out.println(e);

        }
    }
    
    // method booking
    private void booking() {

        try {

            String nama = jTextFieldNama.getText();
            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Nama tidak boleh kosong!"
                );
                return;
            }

            String lapangan = cbLapangan.getSelectedItem().toString();
            
            String statusLapangan = DBLapangan.getStatusLapangan(lapangan);

            if (statusLapangan.equals("maintenance")) {

                JOptionPane.showMessageDialog(
                    this,
                    "Lapangan sedang maintenance!"
                );

                return;
            }

            int idLapangan = 1;

            if (lapangan.equals("Lapangan B")) {
                idLapangan = 2;
            } else if (lapangan.equals("Lapangan VIP")) {
                idLapangan = 3;
            }

            if (jDateChooser1.getDate() == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Pilih tanggal terlebih dahulu!"
                );
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tanggal = sdf.format(jDateChooser1.getDate());
            
            Date tanggalBooking = jDateChooser1.getDate();
            Date hariIni = new Date();

            // reset jam agar hanya compare tanggal
            Calendar calHariIni = Calendar.getInstance();
            calHariIni.setTime(hariIni);
            calHariIni.set(Calendar.HOUR_OF_DAY, 0);
            calHariIni.set(Calendar.MINUTE, 0);
            calHariIni.set(Calendar.SECOND, 0);
            calHariIni.set(Calendar.MILLISECOND, 0);

            Calendar calBooking = Calendar.getInstance();
            calBooking.setTime(tanggalBooking);
            calBooking.set(Calendar.HOUR_OF_DAY, 0);
            calBooking.set(Calendar.MINUTE, 0);
            calBooking.set(Calendar.SECOND, 0);
            calBooking.set(Calendar.MILLISECOND, 0);
            
            // validasi tanggal
            if (calBooking.before(calHariIni)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Tidak bisa booking tanggal yang sudah lewat!"
                );
                return;
            }
            
            Calendar maxBooking = Calendar.getInstance();
            maxBooking.setTime(hariIni);
            maxBooking.add(Calendar.DAY_OF_MONTH, 3);

            maxBooking.set(Calendar.HOUR_OF_DAY, 0);
            maxBooking.set(Calendar.MINUTE, 0);
            maxBooking.set(Calendar.SECOND, 0);
            maxBooking.set(Calendar.MILLISECOND, 0);

            if (calBooking.after(maxBooking)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Booking hanya bisa maksimal H+3!"
                );
                return;
            }

            String jamMulai =
                cbJamMulai.getSelectedItem().toString();
            String jamSelesai =
                cbJamSelesai.getSelectedItem().toString();

            int jamBooking =
                Integer.parseInt(jamMulai.substring(0, 2));

            // validasi jam (realtime)
            Calendar sekarang = Calendar.getInstance();

            int jamSekarang =
                sekarang.get(Calendar.HOUR_OF_DAY);

            // jika booking untuk hari ini
            if (
                calBooking.get(Calendar.YEAR)
                    == calHariIni.get(Calendar.YEAR) &&
                calBooking.get(Calendar.DAY_OF_YEAR)
                    == calHariIni.get(Calendar.DAY_OF_YEAR) &&
                jamBooking < jamSekarang
            ) {

                JOptionPane.showMessageDialog(
                    this, "Jam booking sudah lewat!"
                );

                return;
            }

            int mulai = Integer.parseInt(jamMulai.substring(0, 2));
            int selesai = Integer.parseInt(jamSelesai.substring(0, 2));

            int durasi = selesai - mulai;

            int hargaPerJam = DBLapangan.getHargaLapangan(lapangan);

            int totalHarga = durasi * hargaPerJam;

            // alert konfirmasi
            String pesan =
                    "Konfirmasi Booking:\n\n"
                    + "Lapangan : " + lapangan + "\n"
                    + "Tanggal  : " + tanggal + "\n"
                    + "Jam      : " + jamMulai + " - " + jamSelesai + "\n"
                    + "Durasi   : " + durasi + " jam\n"
                    + "Total    : Rp " + totalHarga;

            int konfirmasi = JOptionPane.showConfirmDialog(
                    this,
                    pesan,
                    "Konfirmasi Booking",
                    JOptionPane.YES_NO_OPTION
            );

            // jika Yes
            if (konfirmasi == JOptionPane.YES_OPTION) {
                boolean bentrok = DBBooking.cekBentrok(
                    idLapangan,
                    tanggal,
                    jamMulai,
                    jamSelesai
                );

                if (bentrok) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Jadwal sudah terisi!",
                            "Booking Gagal",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                DBBooking.tambahBooking(
                        nama,
                        idLapangan,
                        tanggal,
                        jamMulai,
                        jamSelesai,
                        durasi,
                        totalHarga,
                        "aktif"
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Booking berhasil!"
                );

                loadJadwal();
            }

        } catch (Exception e) {

            System.out.println(e);

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldNama = new javax.swing.JTextField();
        cbLapangan = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        cbJamMulai = new javax.swing.JComboBox<>();
        cbJamSelesai = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableJadwal = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButtonAdmin = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblHargaPerJam = new javax.swing.JLabel();
        lblDurasi = new javax.swing.JLabel();
        lblTotalHarga = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));

        jTextFieldNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNamaActionPerformed(evt);
            }
        });

        cbLapangan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbLapangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLapanganActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Lapangan");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Tanggal Booking");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Jam Mulai");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Jam Selesai ");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Booking");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cbJamMulai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbJamMulai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJamMulaiActionPerformed(evt);
            }
        });

        cbJamSelesai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbJamSelesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJamSelesaiActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Nama");

        tableJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Jam", "Lapangan A", "Lapangan B", "Lapangan VIP"
            }
        ));
        jScrollPane1.setViewportView(tableJadwal);

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel7.setText("Jadwal Ketersediaan Lapangan");

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel8.setText("Form Booking Lap. Badminton");

        jButtonAdmin.setText("Admin");
        jButtonAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdminActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Harga Per Jam:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Durasi:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Total:");

        lblHargaPerJam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHargaPerJam.setText("Rp 0");

        lblDurasi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDurasi.setText("0 jam");

        lblTotalHarga.setBackground(new java.awt.Color(245, 245, 245));
        lblTotalHarga.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalHarga.setText("Rp 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblHargaPerJam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDurasi, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                    .addComponent(lblTotalHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblHargaPerJam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblDurasi))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblTotalHarga))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldNama)
                    .addComponent(cbLapangan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbJamMulai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbJamSelesai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jButtonAdmin))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbLapangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbJamMulai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbJamSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNamaActionPerformed

    private void cbLapanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLapanganActionPerformed
        // TODO add your handling code here:
        hitungTotal();
    }//GEN-LAST:event_cbLapanganActionPerformed

    private void cbJamMulaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJamMulaiActionPerformed
        // TODO add your handling code here:
        int index = cbJamMulai.getSelectedIndex();

        if (index < 0) {
            return;
        }

        loadJamSelesai(index);
        hitungTotal();
    }//GEN-LAST:event_cbJamMulaiActionPerformed

    private void cbJamSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJamSelesaiActionPerformed
        // TODO add your handling code here:
        hitungTotal();
    }//GEN-LAST:event_cbJamSelesaiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        booking();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdminActionPerformed
        // TODO add your handling code here:
        new LoginAdminView().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButtonAdminActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        loadJadwal();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserBookingView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserBookingView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserBookingView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserBookingView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserBookingView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbJamMulai;
    private javax.swing.JComboBox<String> cbJamSelesai;
    private javax.swing.JComboBox<String> cbLapangan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdmin;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldNama;
    private javax.swing.JLabel lblDurasi;
    private javax.swing.JLabel lblHargaPerJam;
    private javax.swing.JLabel lblTotalHarga;
    private javax.swing.JTable tableJadwal;
    // End of variables declaration//GEN-END:variables
}
