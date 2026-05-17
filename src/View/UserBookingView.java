/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import Model.BookingModel;
import Model.LapanganModel;
import Controller.BookingController;

/**
 *
 * @author ASUS
 */
public class UserBookingView extends javax.swing.JFrame {
    
    private ArrayList<Integer> listIdLapangan = new ArrayList<>();
    private final BookingController bc = new BookingController();

    public UserBookingView() {
        initComponents();
        
        jDateChooser1.setDate(new Date());
        bc.updateStatusSelesai();
        
        loadJamMulai();
        loadJamSelesai(0);
        loadLapangan();
        loadJadwal();
        
        autoRefresh();
        
    }
    
    private void autoRefresh() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    loadJadwal();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        t.start();
    }

    private void loadJamMulai() {
        cbJamMulai.removeAllItems();
        for (String j : bc.getJamMulai()) {
            cbJamMulai.addItem(j);
        }
    }
    
    private void loadJamSelesai(int mulaiIndex) {
        cbJamSelesai.removeAllItems();
        String[] jam = bc.getJamSelesai();
        for (int i = mulaiIndex; i < jam.length; i++) {
            cbJamSelesai.addItem(jam[i]);
        }
    }
    
    private void loadLapangan() {
        try {
            cbLapangan.removeAllItems();
            ArrayList<LapanganModel> listLapangan =
                    bc.getDataLapangan();
            for (LapanganModel lapangan : listLapangan) {
                cbLapangan.addItem(
                        lapangan.getNamaLapangan()
                );
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private DefaultTableModel createJadwalModel() {
        return new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    
  private ArrayList<String> loadDataLapangan(
    DefaultTableModel model,
    String keyword
) {

        ArrayList<String> listStatusLapangan =
                new ArrayList<>();

        ArrayList<LapanganModel> listLapangan;

if (keyword.isEmpty()) {

    listLapangan =
            bc.getDataLapangan();

} else {

    listLapangan =
            bc.cariLapangan(keyword);
}

        for (LapanganModel lapangan : listLapangan) {

            model.addColumn(
                    lapangan.getNamaLapangan()
            );

            listIdLapangan.add(
                    lapangan.getIdLapangan()
            );

            listStatusLapangan.add(
                    lapangan.getStatus()
            );
        }

        return listStatusLapangan;
    }
    
    private void loadRowJadwal(
        DefaultTableModel model,
        ArrayList<String> listStatusLapangan
    ) {

        String tanggal = getTanggalDipilih();

        for (int jam = 7; jam < 22; jam++) {

            String jamMulai = String.format("%02d:00", jam);

            String jamSelesai = String.format("%02d:00", jam + 1);

            String waktu = jamMulai + " - " + jamSelesai;

            Object[] row = bc.buatRowJadwal(
                        waktu, listIdLapangan,
                        listStatusLapangan,
                        tanggal, jamMulai
                );
            model.addRow(row);
        }
    }
    
private void loadJadwal() {
    loadJadwal("");
}

private void loadJadwal(String keyword) {

    DefaultTableModel model =
            createJadwalModel();

    model.addColumn("Jam");

    listIdLapangan.clear();

    try {

        ArrayList<String> listStatusLapangan =
                loadDataLapangan(
                        model,
                        keyword
                );

        loadRowJadwal(
                model,
                listStatusLapangan
        );

        tableJadwal.setModel(model);

    } catch (Exception e) {

        System.out.println(e);
    }
}
    
    private String getTanggalDipilih() {
        if (jDateChooser1.getDate() == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(jDateChooser1.getDate());
    }
    

    private void hitungTotal() {
        try {            
            if (cbJamMulai.getSelectedItem() == null ||
                cbJamSelesai.getSelectedItem() == null) {
                return;
            }
            
            String jamMulai = cbJamMulai.getSelectedItem().toString();
            String jamSelesai = cbJamSelesai.getSelectedItem().toString();

            int durasi =
                bc.hitungDurasi(
                        jamMulai,
                        jamSelesai
                );

            String lapangan = cbLapangan.getSelectedItem().toString();

            int hargaPerJam = bc.getHargaLapangan(lapangan);

            int total = bc.hitungTotal(durasi, hargaPerJam);

            // tampilkan ke label
            lblHargaPerJam.setText("Rp " + hargaPerJam);
            lblDurasi.setText(durasi + " jam");
            lblTotalHarga.setText("Rp " + total);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private BookingModel ambilDataForm() {

        String nama = jTextFieldNama.getText();
        int index = cbLapangan.getSelectedIndex();
        int idLapangan = listIdLapangan.get(index);
        String tanggal = bc.formatTanggal(jDateChooser1.getDate());  
        String jamMulai = cbJamMulai.getSelectedItem().toString();
        String jamSelesai =cbJamSelesai.getSelectedItem().toString();
        String namaLapangan = cbLapangan.getSelectedItem().toString();

        return bc.buatBooking(
                nama,
                idLapangan,
                tanggal,
                jamMulai,
                jamSelesai,
                namaLapangan
        );
    }
    
    // method booking
    public void booking() {
        try {
            BookingModel booking = ambilDataForm();
            Date tanggalBooking = jDateChooser1.getDate();
            String namaLapangan = cbLapangan.getSelectedItem().toString();
            String pesan = bc.getDetailBooking(
                booking,
                namaLapangan
            );
            
            int konfirmasi = JOptionPane.showConfirmDialog(
                        this, pesan, "Konfirmasi Booking",
                        JOptionPane.YES_NO_OPTION
                    );

            if (konfirmasi == JOptionPane.YES_OPTION) {

                String hasil = bc.prosesBooking(
                        booking,
                        tanggalBooking,
                        namaLapangan
                    );

                JOptionPane.showMessageDialog(
                        this, hasil
                );

                loadJadwal();
            
}

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,  e.getMessage()
            );
}
}

    private void cariLapangan() {

    String keyword =
            CariLapangan.getText();

    loadJadwal(keyword);
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
        CariLapangan = new javax.swing.JTextField();
        Cari = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNamaActionPerformed(evt);
            }
        });
        getContentPane().add(jTextFieldNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 113, 240, 28));

        cbLapangan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbLapangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLapanganActionPerformed(evt);
            }
        });
        getContentPane().add(cbLapangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 169, 240, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Lapangan");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 147, 240, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tanggal Booking");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 203, 240, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Jam Mulai");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 259, 240, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jam Selesai ");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 315, 240, -1));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Booking");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 499, 240, 33));

        cbJamMulai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbJamMulai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJamMulaiActionPerformed(evt);
            }
        });
        getContentPane().add(cbJamMulai, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 281, 240, -1));

        cbJamSelesai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbJamSelesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJamSelesaiActionPerformed(evt);
            }
        });
        getContentPane().add(cbJamSelesai, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 337, 240, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nama");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 91, 240, -1));

        tableJadwal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 605, 390));

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Cek Jadwal Ketersediaan Lapangan");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 350, 42));

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Form Booking");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 42));

        jButtonAdmin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAdmin.setText("Admin");
        jButtonAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdminActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 560, 80, -1));

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 377, 240, -1));

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 225, 240, -1));

        CariLapangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CariLapanganActionPerformed(evt);
            }
        });
        getContentPane().add(CariLapangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 610, 30));

        Cari.setText("Cari");
        Cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CariActionPerformed(evt);
            }
        });
        getContentPane().add(Cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 40, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/bg_login.png.png"))); // NOI18N
        jLabel11.setText("jLabel11");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 940, 610));

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

    private void CariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CariActionPerformed
        // TODO add your handling code here:
        cariLapangan();
    }//GEN-LAST:event_CariActionPerformed

    private void CariLapanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CariLapanganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CariLapanganActionPerformed

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
    private javax.swing.JButton Cari;
    private javax.swing.JTextField CariLapangan;
    private javax.swing.JComboBox<String> cbJamMulai;
    private javax.swing.JComboBox<String> cbJamSelesai;
    private javax.swing.JComboBox<String> cbLapangan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdmin;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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