/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.knet.view;
import com.mycompany.knet.model.SesiModel;
import com.mycompany.knet.repository.SesiRepository;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author asusa
 */
public class FormLaporan extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormLaporan.class.getName());

    /**
     * Creates new form FormLaporan
     */
      private final SesiRepository sesiRepo = new SesiRepository();

    public FormLaporan() {
        initComponents();
        setSize(900, 620);
        setLocationRelativeTo(null);
        aturTampilan();
        pasangTombol();
        tfTanggal.setText(LocalDate.now().toString());
        tampilSemua();
    }
    
        private void aturTampilan() {
        Color slate = new Color(45, 55, 72);
        Color hijau = new Color(113, 153, 139);
        Color krem = new Color(255, 255, 226);
        Color hijauMuda = new Color(173, 209, 198);

        getContentPane().setBackground(Color.WHITE);

        // header
        pnlHeader.setBackground(slate);
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Georgia", Font.BOLD, 18));
        gayaTombol(btnKembali, krem, slate);

        // kartu filter tanggal
        pnlFilter.setBackground(hijau);
        pnlFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTanggal.setForeground(Color.WHITE);
        lblTanggal.setFont(new Font("Gadugi", Font.BOLD, 12));
        tfTanggal.setFont(new Font("Gadugi", Font.PLAIN, 13));
        gayaTombol(btnTanggal, slate, Color.WHITE);
        gayaTombol(btnSemua, krem, slate);

        // kartu total
        pnlTotal.setBackground(krem);
        pnlTotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblKeterangan.setForeground(slate);
        lblKeterangan.setFont(new Font("Gadugi", Font.PLAIN, 13));
        lblTotal.setForeground(slate);
        lblTotal.setFont(new Font("Georgia", Font.BOLD, 28));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        // kartu tabel
        pnlTabel.setBackground(krem);
        pnlTabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        // tabel
        jScrollPane1.getViewport().setBackground(hijauMuda);
        jtLaporan.setBackground(hijauMuda);
        jtLaporan.setFont(new Font("Gadugi", Font.PLAIN, 12));
        jtLaporan.setRowHeight(35);
        jtLaporan.setGridColor(new Color(51, 51, 51));
        jtLaporan.setSelectionBackground(Color.WHITE);
        jtLaporan.setSelectionForeground(Color.BLACK);
        jtLaporan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = jtLaporan.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setPreferredSize(new Dimension(0, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, false, false, row, column);
                setBackground(slate);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(LEFT);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }

    private void gayaTombol(JButton tombol, Color latar, Color tulisan) {
        tombol.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        tombol.setBackground(latar);
        tombol.setForeground(tulisan);
        tombol.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tombol.setFocusPainted(false);
        tombol.setOpaque(true);
        tombol.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(45, 55, 72)),
                javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16)));
    }

    // Semua tombol dipasang lewat kode (tidak perlu klik dua kali di Design)
    private void pasangTombol() {
        btnTanggal.addActionListener(e -> tampilPerTanggal());
        btnSemua.addActionListener(e -> tampilSemua());
        btnKembali.addActionListener(e -> {
            new FormMember().setVisible(true);
            this.dispose();
        });
    }

    private void tampilSemua() {
        isiTabel(sesiRepo.getAllSesi());
        lblKeterangan.setText("Total pendapatan seluruh sesi");
        lblTotal.setText("Rp " + formatAngka(sesiRepo.getTotalPendapatan()));
    }

    private void tampilPerTanggal() {
        String teks = tfTanggal.getText().trim();
        Date tanggal;

        try {
            tanggal = Date.valueOf(teks);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-MM-dd, contoh: 2026-11-09");
            return;
        }

        isiTabel(sesiRepo.getSesiByTanggal(tanggal));
        lblKeterangan.setText("Total pendapatan tanggal " + teks);
        lblTotal.setText("Rp " + formatAngka(sesiRepo.getPendapatanByTanggal(tanggal)));
    }

    private void isiTabel(List<SesiModel> daftarSesi) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DefaultTableModel model = (DefaultTableModel) jtLaporan.getModel();
        model.setRowCount(0);

        for (SesiModel s : daftarSesi) {
            model.addRow(new Object[]{
                s.getIdSesi(),
                s.getNamaMember(),
                s.getNamaKomputer(),
                s.getJamMulai() == null ? "" : format.format(s.getJamMulai()),
                s.getJamSelesai() == null ? "" : format.format(s.getJamSelesai()),
                "Rp " + formatAngka(s.getTotalBiaya())
            });
        }
    }

    private String formatAngka(int angka) {
        return NumberFormat.getInstance(Locale.forLanguageTag("id-ID")).format(angka);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        btnKembali = new javax.swing.JButton();
        pnlFilter = new javax.swing.JPanel();
        lblTanggal = new javax.swing.JLabel();
        tfTanggal = new javax.swing.JTextField();
        btnTanggal = new javax.swing.JButton();
        btnSemua = new javax.swing.JButton();
        pnlTotal = new javax.swing.JPanel();
        lblKeterangan = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        pnlTabel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtLaporan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Knet - Laporan");

        lblJudul.setText("Knet - Laporan");

        btnKembali.setText("Kembali ke Menu");
        btnKembali.addActionListener(this::btnKembaliActionPerformed);

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnKembali)
                .addContainerGap())
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJudul)
                    .addComponent(btnKembali))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        lblTanggal.setText("Tanggal (yyyy-MM-dd)");

        btnTanggal.setText("Tampil per Tanggal");
        btnTanggal.addActionListener(this::btnTanggalActionPerformed);

        btnSemua.setText("Tampil Semua");
        btnSemua.addActionListener(this::btnSemuaActionPerformed);

        javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
        pnlFilter.setLayout(pnlFilterLayout);
        pnlFilterLayout.setHorizontalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addComponent(lblTanggal)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addComponent(tfTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSemua, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlFilterLayout.setVerticalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addComponent(lblTanggal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTanggal)
                    .addComponent(btnSemua))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        lblKeterangan.setText("Total pendapatan seluruh sesi");

        lblTotal.setText("Rp 0");

        javax.swing.GroupLayout pnlTotalLayout = new javax.swing.GroupLayout(pnlTotal);
        pnlTotal.setLayout(pnlTotalLayout);
        pnlTotalLayout.setHorizontalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalLayout.createSequentialGroup()
                .addComponent(lblKeterangan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal)
                .addContainerGap())
        );
        pnlTotalLayout.setVerticalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalLayout.createSequentialGroup()
                .addGroup(pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKeterangan)
                    .addComponent(lblTotal))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jtLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Sesi", "Member", "Komputer", "Jam Mulai", "Jam Selesai", "Total Biaya"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtLaporan.setFillsViewportHeight(true);
        jtLaporan.setRowHeight(35);
        jtLaporan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jtLaporan);

        javax.swing.GroupLayout pnlTabelLayout = new javax.swing.GroupLayout(pnlTabel);
        pnlTabel.setLayout(pnlTabelLayout);
        pnlTabelLayout.setHorizontalGroup(
            pnlTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTabelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTabelLayout.setVerticalGroup(
            pnlTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnTanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTanggalActionPerformed

    private void btnSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSemuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSemuaActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormLaporan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnSemua;
    private javax.swing.JButton btnTanggal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtLaporan;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTabel;
    private javax.swing.JPanel pnlTotal;
    private javax.swing.JTextField tfTanggal;
    // End of variables declaration//GEN-END:variables
}
