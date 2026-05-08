/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;
import Helper.DBLapangan;
import java.sql.ResultSet;
import Helper.DBBooking;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class UserBookingView extends javax.swing.JFrame {

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

        for (int i = mulaiIndex + 1; i < jam.length; i++) {
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

    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("Jam");
    model.addColumn("Lapangan A");
    model.addColumn("Lapangan B");
    model.addColumn("Lapangan VIP");

    model.addRow(new Object[]{
        "07:00 - 08:00",
        "Tersedia",
        "Tersedia",
        "Terisi"
    });

    model.addRow(new Object[]{
        "08:00 - 09:00",
        "Terisi",
        "Tersedia",
        "Tersedia"
    });

    model.addRow(new Object[]{
        "09:00 - 10:00",
        "Tersedia",
        "Terisi",
        "Tersedia"
    });

    tableJadwal.setModel(model);

}
        // method total
        private void hitungTotal() {

    try {

        String jamMulai = cbJamMulai.getSelectedItem().toString();
        String jamSelesai = cbJamSelesai.getSelectedItem().toString();

        int mulai = Integer.parseInt(jamMulai.substring(0, 2));
        int selesai = Integer.parseInt(jamSelesai.substring(0, 2));

        int durasi = selesai - mulai;

        int hargaPerJam = 50000;

        String lapangan = cbLapangan.getSelectedItem().toString();

        if (lapangan.equals("Lapangan VIP")) {
            hargaPerJam = 100000;
        }

        int total = durasi * hargaPerJam;

        TotalHarga.setText("Rp " + total);

    } catch (Exception e) {

        System.out.println(e);

    }
}
        // method booking
        private void booking() {

    try {

        String nama = jTextFieldNama.getText();

        String lapangan = cbLapangan.getSelectedItem().toString();

        int idLapangan = 1;

        if (lapangan.equals("Lapangan B")) {
            idLapangan = 2;
        } else if (lapangan.equals("Lapangan VIP")) {
            idLapangan = 3;
        }

        String tanggal = jTextFieldTanggal.getText();

        String jamMulai = cbJamMulai.getSelectedItem().toString();
        String jamSelesai = cbJamSelesai.getSelectedItem().toString();

        int mulai = Integer.parseInt(jamMulai.substring(0, 2));
        int selesai = Integer.parseInt(jamSelesai.substring(0, 2));

        int durasi = selesai - mulai;

        int hargaPerJam = 50000;

        if (lapangan.equals("Lapangan VIP")) {
            hargaPerJam = 100000;
        }

        int totalHarga = durasi * hargaPerJam;

        // ALERT KONFIRMASI

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

        // =========================
        // JIKA YES
        // =========================

        if (konfirmasi == JOptionPane.YES_OPTION) {

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
        jTextFieldTanggal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableJadwal = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButtonAdmin = new javax.swing.JButton();
        TotalHarga = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextFieldNama.setText("Masukkan Nama ");
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

        jLabel1.setText("Lapangan");

        jLabel2.setText("Tanggal Booking");

        jLabel3.setText("Jam Mulai");

        jLabel4.setText("Jam Selesai ");

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

        jTextFieldTanggal.setText("2026-05-08");
        jTextFieldTanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTanggalActionPerformed(evt);
            }
        });

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

        jLabel7.setText("Jadwal Ketersediaan Lapangan");

        jLabel8.setText("Form Booking Lap. Badminton");

        jLabel9.setText("Total");

        jButtonAdmin.setText("Admin");

        TotalHarga.setText("Rp.0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldNama)
                    .addComponent(cbLapangan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldTanggal)
                    .addComponent(cbJamMulai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbJamSelesai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TotalHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAdmin)))
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addComponent(jButtonAdmin))
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(jTextFieldTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbJamMulai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbJamSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(TotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
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
        loadJamSelesai(index);
        hitungTotal();
    }//GEN-LAST:event_cbJamMulaiActionPerformed

    private void jTextFieldTanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTanggalActionPerformed

    private void cbJamSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJamSelesaiActionPerformed
        // TODO add your handling code here:
        hitungTotal();
    }//GEN-LAST:event_cbJamSelesaiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        booking();
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JLabel TotalHarga;
    private javax.swing.JComboBox<String> cbJamMulai;
    private javax.swing.JComboBox<String> cbJamSelesai;
    private javax.swing.JComboBox<String> cbLapangan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdmin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldNama;
    private javax.swing.JTextField jTextFieldTanggal;
    private javax.swing.JTable tableJadwal;
    // End of variables declaration//GEN-END:variables
}
