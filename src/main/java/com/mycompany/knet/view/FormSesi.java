/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.knet.view;
import com.mycompany.knet.model.KomputerModel;
import com.mycompany.knet.model.MemberModel;
import com.mycompany.knet.model.SesiModel;
import com.mycompany.knet.repository.KomputerRepository;
import com.mycompany.knet.repository.MemberRepository;
import com.mycompany.knet.repository.SesiRepository;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asusa
 */
public class FormSesi extends javax.swing.JFrame {
    
        private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormSesi.class.getName());

    private MemberRepository memberRepo = new MemberRepository();
    private KomputerRepository komputerRepo = new KomputerRepository();
    private SesiRepository sesiRepo = new SesiRepository();
    private List<MemberModel> daftarMember;
    private List<KomputerModel> daftarKomputer;

    public FormSesi() {
        initComponents();
        setSize(900, 620);
        setLocationRelativeTo(null);
        aturTampilan();
        pasangTombol();
        muatUlang();
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

        // kartu "Mulai Sesi Baru"
        pnlMulai.setBackground(hijau);
        pnlMulai.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        for (JLabel l : new JLabel[]{lblMulai, lblMember, lblKomputer}) {
            l.setForeground(Color.WHITE);
            l.setFont(new Font("Gadugi", Font.BOLD, 12));
        }
        lblMulai.setFont(new Font("Gadugi", Font.BOLD, 15));
        cbMember.setFont(new Font("Gadugi", Font.PLAIN, 13));
        cbKomputer.setFont(new Font("Gadugi", Font.PLAIN, 13));
        gayaTombol(btnMulai, krem, slate);
        gayaTombol(btnAkhiri, krem, slate);
        gayaTombol(btnMuat, slate, Color.WHITE);

        // kartu tabel
        pnlTabel.setBackground(krem);
        pnlTabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblBerjalan.setForeground(slate);
        lblBerjalan.setFont(new Font("Gadugi", Font.BOLD, 15));
        lblPetunjuk.setForeground(slate);
        lblPetunjuk.setFont(new Font("Gadugi", Font.PLAIN, 12));

        // tabel
        jScrollPane1.getViewport().setBackground(hijauMuda);
        jtSesi.setBackground(hijauMuda);
        jtSesi.setFont(new Font("Gadugi", Font.PLAIN, 12));
        jtSesi.setRowHeight(35);
        jtSesi.setGridColor(new Color(51, 51, 51));
        jtSesi.setSelectionBackground(Color.WHITE);
        jtSesi.setSelectionForeground(Color.BLACK);
        jtSesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // header tabel (pakai renderer supaya warnanya pasti berubah)
        JTableHeader header = jtSesi.getTableHeader();
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
        // BasicButtonUI memastikan warna tombol tampil apa pun tema Java-nya
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
     
       private void pasangTombol() {
        btnMulai.addActionListener(e -> mulaiSesi());
        btnMuat.addActionListener(e -> muatUlang());
    }
       
        private void muatUlang() {
        daftarMember = memberRepo.getAllMembers();
        cbMember.removeAllItems();
        for (MemberModel m : daftarMember) {
            cbMember.addItem(m.getIdMember() + " - " + m.getNama());
        }

        daftarKomputer = komputerRepo.getKomputerTersedia();
        cbKomputer.removeAllItems();
        for (KomputerModel k : daftarKomputer) {
            cbKomputer.addItem(k.getNamaKomputer() + " (Rp " + k.getTarifPerJam() + "/jam)");
        }

        tampilSesiBerjalan();
    }

    private void tampilSesiBerjalan() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DefaultTableModel model = (DefaultTableModel) jtSesi.getModel();
        model.setRowCount(0);

        for (SesiModel s : sesiRepo.getSesiBerjalan()) {
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
        pnlMulai = new javax.swing.JPanel();
        lblMulai = new javax.swing.JLabel();
        lblMember = new javax.swing.JLabel();
        lblKomputer = new javax.swing.JLabel();
        cbMember = new javax.swing.JComboBox<>();
        cbKomputer = new javax.swing.JComboBox<>();
        btnMulai = new javax.swing.JButton();
        btnAkhiri = new javax.swing.JButton();
        btnMuat = new javax.swing.JButton();
        pnlTabel = new javax.swing.JPanel();
        lblBerjalan = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtSesi = new javax.swing.JTable();
        lblPetunjuk = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Knet - Sesi Pemakaian");
        setBackground(new java.awt.Color(255, 255, 255));

        pnlHeader.setBackground(new java.awt.Color(45, 55, 72));
        pnlHeader.setForeground(new java.awt.Color(255, 255, 255));

        lblJudul.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(255, 255, 204));
        lblJudul.setText("Knet - Sesi Pemakaian");

        btnKembali.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
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
                .addGap(25, 25, 25))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJudul)
                    .addComponent(btnKembali))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pnlMulai.setBackground(new java.awt.Color(113, 153, 139));
        pnlMulai.setForeground(new java.awt.Color(255, 255, 204));

        lblMulai.setBackground(new java.awt.Color(255, 255, 204));
        lblMulai.setFont(new java.awt.Font("Gadugi", 1, 16)); // NOI18N
        lblMulai.setForeground(new java.awt.Color(255, 255, 204));
        lblMulai.setText("Mulai Sesi Baru");

        lblMember.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        lblMember.setForeground(new java.awt.Color(255, 255, 255));
        lblMember.setText("Member");

        lblKomputer.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        lblKomputer.setForeground(new java.awt.Color(255, 255, 255));
        lblKomputer.setText("Komputer Tersedia");

        cbMember.setFont(new java.awt.Font("Gadugi", 0, 13)); // NOI18N
        cbMember.setForeground(new java.awt.Color(45, 55, 72));
        cbMember.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbKomputer.setFont(new java.awt.Font("Gadugi", 0, 13)); // NOI18N
        cbKomputer.setForeground(new java.awt.Color(45, 55, 72));
        cbKomputer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnMulai.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        btnMulai.setForeground(new java.awt.Color(45, 55, 72));
        btnMulai.setText("Mulai Sesi");
        btnMulai.addActionListener(this::btnMulaiActionPerformed);

        btnAkhiri.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        btnAkhiri.setForeground(new java.awt.Color(45, 55, 72));
        btnAkhiri.setText("Akhiri Sesi");
        btnAkhiri.addActionListener(this::btnAkhiriActionPerformed);

        btnMuat.setBackground(new java.awt.Color(45, 55, 72));
        btnMuat.setFont(new java.awt.Font("Gadugi", 1, 12)); // NOI18N
        btnMuat.setForeground(new java.awt.Color(255, 255, 255));
        btnMuat.setText("Muat Ulang");
        btnMuat.addActionListener(this::btnMuatActionPerformed);

        javax.swing.GroupLayout pnlMulaiLayout = new javax.swing.GroupLayout(pnlMulai);
        pnlMulai.setLayout(pnlMulaiLayout);
        pnlMulaiLayout.setHorizontalGroup(
            pnlMulaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMulaiLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlMulaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlMulaiLayout.createSequentialGroup()
                        .addComponent(btnMulai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAkhiri)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMuat)
                        .addGap(0, 300, Short.MAX_VALUE))
                    .addGroup(pnlMulaiLayout.createSequentialGroup()
                        .addGroup(pnlMulaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMulaiLayout.createSequentialGroup()
                                .addComponent(lblMember)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblKomputer))
                            .addGroup(pnlMulaiLayout.createSequentialGroup()
                                .addComponent(lblMulai)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlMulaiLayout.createSequentialGroup()
                                .addComponent(cbMember, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(cbKomputer, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );

        pnlMulaiLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cbKomputer, cbMember});

        pnlMulaiLayout.setVerticalGroup(
            pnlMulaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMulaiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMulai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMulaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMember)
                    .addComponent(lblKomputer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMulaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbKomputer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMulaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMulai)
                    .addComponent(btnAkhiri)
                    .addComponent(btnMuat))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pnlTabel.setBackground(new java.awt.Color(255, 255, 226));

        lblBerjalan.setFont(new java.awt.Font("Gadugi", 1, 16)); // NOI18N
        lblBerjalan.setForeground(new java.awt.Color(45, 55, 72));
        lblBerjalan.setText("Sesi yang sedang berjalan");

        jScrollPane1.setBackground(new java.awt.Color(173, 209, 198));

        jtSesi.setForeground(new java.awt.Color(45, 55, 72));
        jtSesi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
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
        jtSesi.setFillsViewportHeight(true);
        jtSesi.setGridColor(new java.awt.Color(255, 255, 255));
        jtSesi.setRowHeight(35);
        jtSesi.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jtSesi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jtSesi.setShowGrid(true);
        jScrollPane1.setViewportView(jtSesi);

        lblPetunjuk.setFont(new java.awt.Font("Gadugi", 0, 12)); // NOI18N
        lblPetunjuk.setText("Pilih satu baris, lalu klik Akhiri Sesi");

        javax.swing.GroupLayout pnlTabelLayout = new javax.swing.GroupLayout(pnlTabel);
        pnlTabel.setLayout(pnlTabelLayout);
        pnlTabelLayout.setHorizontalGroup(
            pnlTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(pnlTabelLayout.createSequentialGroup()
                        .addComponent(lblPetunjuk)
                        .addGap(284, 284, 284))
                    .addGroup(pnlTabelLayout.createSequentialGroup()
                        .addComponent(lblBerjalan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnlTabelLayout.setVerticalGroup(
            pnlTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblBerjalan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPetunjuk, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlMulai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
            .addComponent(pnlTabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMulai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        new FormMember().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnAkhiriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAkhiriActionPerformed
        int baris = jtSesi.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih satu baris sesi yang ingin diakhiri!");
            return;
        }

        int idSesi = Integer.parseInt(jtSesi.getValueAt(baris, 0).toString());
        int pilihan = JOptionPane.showConfirmDialog(this, "Akhiri sesi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (pilihan != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int totalBiaya = sesiRepo.finishSesi(idSesi);
            JOptionPane.showMessageDialog(this, "Sesi selesai. Total biaya: Rp " + formatAngka(totalBiaya));
            muatUlang();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnAkhiriActionPerformed

    private void btnMulaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMulaiActionPerformed
        int iMember = cbMember.getSelectedIndex();
        int iKomputer = cbKomputer.getSelectedIndex();

        if (iMember == -1 || iKomputer == -1) {
            JOptionPane.showMessageDialog(this, "Pilih member dan komputer yang tersedia dulu!");
            return;
        }

        try {
            SesiModel sesi = new SesiModel();
            sesi.setIdMember(daftarMember.get(iMember).getIdMember());
            sesi.setIdKomputer(daftarKomputer.get(iKomputer).getIdKomputer());
            sesiRepo.addSesi(sesi);

            JOptionPane.showMessageDialog(this, "Sesi berhasil dimulai.");
            muatUlang();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnMulaiActionPerformed

    private void btnMuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuatActionPerformed
        muatUlang();
    }//GEN-LAST:event_btnMuatActionPerformed

    
    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormSesi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAkhiri;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnMuat;
    private javax.swing.JButton btnMulai;
    private javax.swing.JComboBox<String> cbKomputer;
    private javax.swing.JComboBox<String> cbMember;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtSesi;
    private javax.swing.JLabel lblBerjalan;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKomputer;
    private javax.swing.JLabel lblMember;
    private javax.swing.JLabel lblMulai;
    private javax.swing.JLabel lblPetunjuk;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMulai;
    private javax.swing.JPanel pnlTabel;
    // End of variables declaration//GEN-END:variables
}
