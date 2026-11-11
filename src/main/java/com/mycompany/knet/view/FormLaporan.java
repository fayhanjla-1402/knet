package knet.view;

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
import knet.entity.Sesi;
import knet.model.SesiTableModel;
import knet.repository.SesiRepository;

public class FormLaporan extends JFrame implements ActionListener {

    private JTextField txtTanggal;
    private JButton btnTampilTanggal;
    private JButton btnTampilSemua;
    private JLabel lblTotalPendapatan;
    private JTable tblRiwayat;
    private SesiTableModel tableModel;
    private SesiRepository repository;

    public FormLaporan() {
        repository = new SesiRepository();
        tableModel = new SesiTableModel();
        siapkanTampilan();
        tampilSemuaRiwayat();
    }

    private void siapkanTampilan() {
        setTitle("Knet - Laporan");
        setSize(700, 520);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblTanggal = new JLabel("Tanggal (yyyy-MM-dd)");
        lblTanggal.setBounds(20, 20, 150, 25);
        add(lblTanggal);

        Date hariIni = new Date(System.currentTimeMillis());
        txtTanggal = new JTextField(hariIni.toString());
        txtTanggal.setBounds(180, 20, 150, 25);
        add(txtTanggal);

        btnTampilTanggal = new JButton("Tampil per Tanggal");
        btnTampilTanggal.setBounds(340, 20, 160, 25);
        btnTampilTanggal.addActionListener(this);
        add(btnTampilTanggal);

        btnTampilSemua = new JButton("Tampil Semua");
        btnTampilSemua.setBounds(510, 20, 140, 25);
        btnTampilSemua.addActionListener(this);
        add(btnTampilSemua);

        lblTotalPendapatan = new JLabel("Total pendapatan: Rp 0");
        lblTotalPendapatan.setBounds(20, 60, 500, 25);
        add(lblTotalPendapatan);

        tblRiwayat = new JTable(tableModel);

        JScrollPane scrollTabel = new JScrollPane(tblRiwayat);
        scrollTabel.setBounds(20, 100, 640, 360);
        add(scrollTabel);
    }

    private Date inputTanggal() {
        String teksTanggal = txtTanggal.getText().trim();
        Date tanggal = null;

        try {
            tanggal = Date.valueOf(teksTanggal);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-MM-dd, contoh 2026-11-10");
            return null;
        }

        return tanggal;
    }

    private void tampilRiwayatPerTanggal() {
        Date tanggal = inputTanggal();

        if (tanggal == null) {
            return;
        }

        List<Sesi> daftarSesi = repository.ambilRiwayatPerTanggal(tanggal);
        int totalPendapatan = repository.ambilPendapatanPerTanggal(tanggal);

        tableModel.setDaftarSesi(daftarSesi);
        lblTotalPendapatan.setText("Total pendapatan " + tanggal.toString() + ": Rp " + totalPendapatan);
    }

    private void tampilSemuaRiwayat() {
        List<Sesi> daftarSesi = repository.ambilRiwayat();
        int totalPendapatan = repository.ambilPendapatanTotal();

        tableModel.setDaftarSesi(daftarSesi);
        lblTotalPendapatan.setText("Total pendapatan seluruh sesi: Rp " + totalPendapatan);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTampilTanggal) {
            tampilRiwayatPerTanggal();
        } else if (e.getSource() == btnTampilSemua) {
            tampilSemuaRiwayat();
        }
    }
}
