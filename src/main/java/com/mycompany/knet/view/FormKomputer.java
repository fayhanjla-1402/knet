package com.mycompany.knet.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.mycompany.knet.entity.Komputer;
import com.mycompany.knet.model.KomputerTableModel;
import com.mycompany.knet.repository.KomputerRepository;

public class FormKomputer extends JFrame implements ActionListener, ListSelectionListener {

    private JTextField txtNamaKomputer;
    private JTextField txtTarifPerJam;
    private JComboBox<String> cmbStatus;
    private JTextField txtCari;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JButton btnCari;
    private JButton btnTampilSemua;
    private JTable tblKomputer;
    private KomputerTableModel tableModel;
    private KomputerRepository repository;
    private int idTerpilih;

    public FormKomputer() {
        repository = new KomputerRepository();
        tableModel = new KomputerTableModel();
        idTerpilih = 0;
        siapkanTampilan();
        tampilKomputer();
    }

    private void siapkanTampilan() {
        setTitle("Knet - Data Komputer");
        setSize(620, 520);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblNamaKomputer = new JLabel("Nama Komputer");
        lblNamaKomputer.setBounds(20, 20, 120, 25);
        add(lblNamaKomputer);

        txtNamaKomputer = new JTextField();
        txtNamaKomputer.setBounds(150, 20, 200, 25);
        add(txtNamaKomputer);

        JLabel lblTarifPerJam = new JLabel("Tarif per Jam");
        lblTarifPerJam.setBounds(20, 55, 120, 25);
        add(lblTarifPerJam);

        txtTarifPerJam = new JTextField();
        txtTarifPerJam.setBounds(150, 55, 200, 25);
        add(txtTarifPerJam);

        JLabel lblStatus = new JLabel("Status");
        lblStatus.setBounds(20, 90, 120, 25);
        add(lblStatus);

        cmbStatus = new JComboBox<String>();
        cmbStatus.addItem("Tersedia");
        cmbStatus.addItem("Dipakai");
        cmbStatus.setBounds(150, 90, 200, 25);
        add(cmbStatus);

        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 130, 100, 30);
        btnSimpan.addActionListener(this);
        add(btnSimpan);

        btnUbah = new JButton("Ubah");
        btnUbah.setBounds(130, 130, 100, 30);
        btnUbah.addActionListener(this);
        add(btnUbah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(240, 130, 100, 30);
        btnHapus.addActionListener(this);
        add(btnHapus);

        btnBersihkan = new JButton("Bersihkan");
        btnBersihkan.setBounds(350, 130, 110, 30);
        btnBersihkan.addActionListener(this);
        add(btnBersihkan);

        JLabel lblCari = new JLabel("Cari Komputer");
        lblCari.setBounds(20, 180, 120, 25);
        add(lblCari);

        txtCari = new JTextField();
        txtCari.setBounds(150, 180, 200, 25);
        add(txtCari);

        btnCari = new JButton("Cari");
        btnCari.setBounds(360, 180, 80, 25);
        btnCari.addActionListener(this);
        add(btnCari);

        btnTampilSemua = new JButton("Tampil Semua");
        btnTampilSemua.setBounds(450, 180, 130, 25);
        btnTampilSemua.addActionListener(this);
        add(btnTampilSemua);

        tblKomputer = new JTable(tableModel);
        tblKomputer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKomputer.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollTabel = new JScrollPane(tblKomputer);
        scrollTabel.setBounds(20, 220, 560, 240);
        add(scrollTabel);
    }

    private Komputer inputKomputer() {
        String namaKomputer = txtNamaKomputer.getText().trim();
        String tarifTeks = txtTarifPerJam.getText().trim();
        int tarifPerJam = 0;

        if (namaKomputer.equals("")) {
            JOptionPane.showMessageDialog(this, "Nama komputer wajib diisi");
            return null;
        }

        if (tarifTeks.equals("")) {
            JOptionPane.showMessageDialog(this, "Tarif per jam wajib diisi");
            return null;
        }

        try {
            tarifPerJam = Integer.parseInt(tarifTeks);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tarif per jam harus berupa angka");
            return null;
        }

        if (tarifPerJam <= 0) {
            JOptionPane.showMessageDialog(this, "Tarif per jam harus lebih dari 0");
            return null;
        }

        Komputer komputer = new Komputer();
        komputer.setNamaKomputer(namaKomputer);
        komputer.setTarifPerJam(tarifPerJam);
        komputer.setStatus(cmbStatus.getSelectedItem().toString());
        return komputer;
    }

    private void simpanKomputer() {
        Komputer komputer = inputKomputer();

        if (komputer == null) {
            return;
        }

        boolean berhasil = repository.simpanData(komputer);

        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Data komputer berhasil disimpan");
            kosongkanForm();
            tampilKomputer();
        } else {
            JOptionPane.showMessageDialog(this, "Data gagal disimpan, nama komputer mungkin sudah dipakai");
        }
    }

    private void ubahKomputer() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu");
            return;
        }

        Komputer komputer = inputKomputer();

        if (komputer == null) {
            return;
        }

        komputer.setIdKomputer(idTerpilih);
        boolean berhasil = repository.ubahData(komputer);

        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Data komputer berhasil diubah");
            kosongkanForm();
            tampilKomputer();
        } else {
            JOptionPane.showMessageDialog(this, "Data gagal diubah, nama komputer mungkin sudah dipakai");
        }
    }

    private void hapusKomputer() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu");
            return;
        }

        int pilihan = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (pilihan != JOptionPane.YES_OPTION) {
            return;
        }

        boolean berhasil = repository.hapusData(idTerpilih);

        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Data komputer berhasil dihapus");
            kosongkanForm();
            tampilKomputer();
        } else {
            JOptionPane.showMessageDialog(this, "Data gagal dihapus, komputer ini sudah tercatat di data sesi");
        }
    }

    private void cariKomputer() {
        String kataKunci = txtCari.getText().trim();
        List<Komputer> hasilCari = repository.cariData(kataKunci);
        kosongkanForm();
        tableModel.setDaftarKomputer(hasilCari);
    }

    private void tampilKomputer() {
        List<Komputer> daftarKomputer = repository.ambilSemuaData();
        tableModel.setDaftarKomputer(daftarKomputer);
    }

    private void tampilSemuaKomputer() {
        txtCari.setText("");
        kosongkanForm();
        tampilKomputer();
    }

    private void isiFormDariTabel(int baris) {
        Komputer komputer = tableModel.getKomputerAt(baris);
        idTerpilih = komputer.getIdKomputer();
        txtNamaKomputer.setText(komputer.getNamaKomputer());
        txtTarifPerJam.setText(String.valueOf(komputer.getTarifPerJam()));
        cmbStatus.setSelectedItem(komputer.getStatus());
    }

    private void kosongkanForm() {
        idTerpilih = 0;
        txtNamaKomputer.setText("");
        txtTarifPerJam.setText("");
        cmbStatus.setSelectedIndex(0);
        tblKomputer.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSimpan) {
            simpanKomputer();
        } else if (e.getSource() == btnUbah) {
            ubahKomputer();
        } else if (e.getSource() == btnHapus) {
            hapusKomputer();
        } else if (e.getSource() == btnBersihkan) {
            kosongkanForm();
        } else if (e.getSource() == btnCari) {
            cariKomputer();
        } else if (e.getSource() == btnTampilSemua) {
            tampilSemuaKomputer();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int baris = tblKomputer.getSelectedRow();

        if (baris >= 0) {
            isiFormDariTabel(baris);
        }
    }
}
