package com.mycompany.knet.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.mycompany.knet.entity.Member;
import com.mycompany.knet.model.MemberTableModel;
import com.mycompany.knet.repository.MemberRepository;

public class FormMember extends JFrame implements ActionListener, ListSelectionListener {

    private JTextField txtNama;
    private JTextField txtNoHp;
    private JTextField txtTanggalDaftar;
    private JTextField txtCari;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JButton btnCari;
    private JButton btnTampilSemua;
    private JTable tblMember;
    private MemberTableModel tableModel;
    private MemberRepository repository;
    private int idTerpilih;

    public FormMember() {
        repository = new MemberRepository();
        tableModel = new MemberTableModel();
        idTerpilih = 0;
        siapkanTampilan();
        tampilMember();
    }

    private void siapkanTampilan() {
        setTitle("Knet - Data Member");
        setSize(620, 520);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblNama = new JLabel("Nama");
        lblNama.setBounds(20, 20, 120, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(150, 20, 200, 25);
        add(txtNama);

        JLabel lblNoHp = new JLabel("No HP");
        lblNoHp.setBounds(20, 55, 120, 25);
        add(lblNoHp);

        txtNoHp = new JTextField();
        txtNoHp.setBounds(150, 55, 200, 25);
        add(txtNoHp);

        JLabel lblTanggalDaftar = new JLabel("Tanggal Daftar");
        lblTanggalDaftar.setBounds(20, 90, 120, 25);
        add(lblTanggalDaftar);

        txtTanggalDaftar = new JTextField();
        txtTanggalDaftar.setBounds(150, 90, 200, 25);
        txtTanggalDaftar.setEditable(false);
        add(txtTanggalDaftar);

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

        JLabel lblCari = new JLabel("Cari Member");
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

        tblMember = new JTable(tableModel);
        tblMember.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMember.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollTabel = new JScrollPane(tblMember);
        scrollTabel.setBounds(20, 220, 560, 240);
        add(scrollTabel);
    }

    private Member inputMember() {
        String nama = txtNama.getText().trim();
        String noHp = txtNoHp.getText().trim();

        if (nama.equals("")) {
            JOptionPane.showMessageDialog(this, "Nama wajib diisi");
            return null;
        }

        if (noHp.length() > 15) {
            JOptionPane.showMessageDialog(this, "No HP maksimal 15 digit");
            return null;
        }

        if (!noHp.equals("") && !apakahAngkaSemua(noHp)) {
            JOptionPane.showMessageDialog(this, "No HP hanya boleh berisi angka");
            return null;
        }

        Member member = new Member();
        member.setNama(nama);
        member.setNoHp(noHp);
        return member;
    }

    private boolean apakahAngkaSemua(String teks) {
        for (int i = 0; i < teks.length(); i++) {
            char karakter = teks.charAt(i);

            if (!Character.isDigit(karakter)) {
                return false;
            }
        }

        return true;
    }

    private void simpanMember() {
        Member member = inputMember();

        if (member == null) {
            return;
        }

        Date hariIni = new Date(System.currentTimeMillis());
        member.setTanggalDaftar(hariIni);

        boolean berhasil = repository.simpanData(member);

        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Data member berhasil disimpan");
            kosongkanForm();
            tampilMember();
        } else {
            JOptionPane.showMessageDialog(this, "Data member gagal disimpan");
        }
    }

    private void ubahMember() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu");
            return;
        }

        Member member = inputMember();

        if (member == null) {
            return;
        }

        member.setIdMember(idTerpilih);
        boolean berhasil = repository.ubahData(member);

        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Data member berhasil diubah");
            kosongkanForm();
            tampilMember();
        } else {
            JOptionPane.showMessageDialog(this, "Data member gagal diubah");
        }
    }

    private void hapusMember() {
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
            JOptionPane.showMessageDialog(this, "Data member berhasil dihapus");
            kosongkanForm();
            tampilMember();
        } else {
            JOptionPane.showMessageDialog(this, "Data gagal dihapus, member ini sudah tercatat di data sesi");
        }
    }

    private void cariMember() {
        String kataKunci = txtCari.getText().trim();
        List<Member> hasilCari = repository.cariData(kataKunci);
        kosongkanForm();
        tableModel.setDaftarMember(hasilCari);
    }

    private void tampilMember() {
        List<Member> daftarMember = repository.ambilSemuaData();
        tableModel.setDaftarMember(daftarMember);
    }

    private void tampilSemuaMember() {
        txtCari.setText("");
        kosongkanForm();
        tampilMember();
    }

    private void isiFormDariTabel(int baris) {
        Member member = tableModel.getMemberAt(baris);
        idTerpilih = member.getIdMember();
        txtNama.setText(member.getNama());
        txtNoHp.setText(member.getNoHp());
        txtTanggalDaftar.setText(member.getTanggalDaftar().toString());
    }

    private void kosongkanForm() {
        idTerpilih = 0;
        txtNama.setText("");
        txtNoHp.setText("");
        txtTanggalDaftar.setText("");
        tblMember.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSimpan) {
            simpanMember();
        } else if (e.getSource() == btnUbah) {
            ubahMember();
        } else if (e.getSource() == btnHapus) {
            hapusMember();
        } else if (e.getSource() == btnBersihkan) {
            kosongkanForm();
        } else if (e.getSource() == btnCari) {
            cariMember();
        } else if (e.getSource() == btnTampilSemua) {
            tampilSemuaMember();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int baris = tblMember.getSelectedRow();

        if (baris >= 0) {
            isiFormDariTabel(baris);
        }
    }
}
