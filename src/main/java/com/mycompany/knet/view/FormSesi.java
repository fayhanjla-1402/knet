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
import javax.swing.ListSelectionModel;
import com.mycompany.knet.entity.Komputer;
import com.mycompany.knet.entity.Member;
import com.mycompany.knet.entity.Sesi;
import com.mycompany.knet.model.SesiTableModel;
import com.mycompany.knet.repository.KomputerRepository;
import com.mycompany.knet.repository.MemberRepository;
import com.mycompany.knet.repository.SesiRepository;

public class FormSesi extends JFrame implements ActionListener {

    private JComboBox<String> cmbMember;
    private JComboBox<String> cmbKomputer;
    private JButton btnMulai;
    private JButton btnAkhiri;
    private JButton btnMuatUlang;
    private JTable tblSesi;
    private SesiTableModel tableModel;
    private SesiRepository sesiRepository;
    private MemberRepository memberRepository;
    private KomputerRepository komputerRepository;
    private List<Member> daftarMember;
    private List<Komputer> daftarKomputer;

    public FormSesi() {
        sesiRepository = new SesiRepository();
        memberRepository = new MemberRepository();
        komputerRepository = new KomputerRepository();
        tableModel = new SesiTableModel();
        siapkanTampilan();
        muatData();
    }

    private void siapkanTampilan() {
        setTitle("Knet - Sesi Pemakaian");
        setSize(700, 520);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblMember = new JLabel("Member");
        lblMember.setBounds(20, 20, 120, 25);
        add(lblMember);

        cmbMember = new JComboBox<String>();
        cmbMember.setBounds(150, 20, 300, 25);
        add(cmbMember);

        JLabel lblKomputer = new JLabel("Komputer Tersedia");
        lblKomputer.setBounds(20, 55, 130, 25);
        add(lblKomputer);

        cmbKomputer = new JComboBox<String>();
        cmbKomputer.setBounds(150, 55, 300, 25);
        add(cmbKomputer);

        btnMulai = new JButton("Mulai Sesi");
        btnMulai.setBounds(20, 95, 130, 30);
        btnMulai.addActionListener(this);
        add(btnMulai);

        btnAkhiri = new JButton("Akhiri Sesi");
        btnAkhiri.setBounds(160, 95, 130, 30);
        btnAkhiri.addActionListener(this);
        add(btnAkhiri);

        btnMuatUlang = new JButton("Muat Ulang");
        btnMuatUlang.setBounds(300, 95, 130, 30);
        btnMuatUlang.addActionListener(this);
        add(btnMuatUlang);

        JLabel lblTabel = new JLabel("Sesi yang sedang berjalan (pilih satu baris lalu klik Akhiri Sesi)");
        lblTabel.setBounds(20, 145, 500, 25);
        add(lblTabel);

        tblSesi = new JTable(tableModel);
        tblSesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabel = new JScrollPane(tblSesi);
        scrollTabel.setBounds(20, 175, 640, 280);
        add(scrollTabel);
    }

    private void isiComboMember() {
        daftarMember = memberRepository.ambilSemuaData();
        cmbMember.removeAllItems();

        for (int i = 0; i < daftarMember.size(); i++) {
            Member member = daftarMember.get(i);
            cmbMember.addItem(member.getIdMember() + " - " + member.getNama());
        }
    }

    private void isiComboKomputer() {
        daftarKomputer = komputerRepository.ambilKomputerTersedia();
        cmbKomputer.removeAllItems();

        for (int i = 0; i < daftarKomputer.size(); i++) {
            Komputer komputer = daftarKomputer.get(i);
            cmbKomputer.addItem(komputer.getNamaKomputer() + " (Rp " + komputer.getTarifPerJam() + "/jam)");
        }
    }

    private void mulaiSesi() {
        int indexMember = cmbMember.getSelectedIndex();
        int indexKomputer = cmbKomputer.getSelectedIndex();

        if (indexMember < 0) {
            JOptionPane.showMessageDialog(this, "Belum ada data member");
            return;
        }

        if (indexKomputer < 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada komputer yang tersedia");
            return;
        }

        Member member = daftarMember.get(indexMember);
        Komputer komputer = daftarKomputer.get(indexKomputer);

        boolean berhasil = sesiRepository.mulaiSesi(member.getIdMember(), komputer.getIdKomputer());

        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Sesi dimulai untuk " + member.getNama() + " di " + komputer.getNamaKomputer());
            muatData();
        } else {
            JOptionPane.showMessageDialog(this, "Sesi gagal dimulai");
        }
    }

    private void akhiriSesi() {
        int baris = tblSesi.getSelectedRow();

        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih sesi di tabel terlebih dahulu");
            return;
        }

        Sesi sesi = tableModel.getSesiAt(baris);
        int totalBiaya = sesiRepository.akhiriSesi(sesi.getIdSesi());

        if (totalBiaya >= 0) {
            JOptionPane.showMessageDialog(this, "Sesi selesai. Total biaya: Rp " + totalBiaya);
            muatData();
        } else {
            JOptionPane.showMessageDialog(this, "Sesi gagal diakhiri");
        }
    }

    private void muatData() {
        isiComboMember();
        isiComboKomputer();
        tampilSesiBerjalan();
    }

    private void tampilSesiBerjalan() {
        List<Sesi> daftarSesi = sesiRepository.ambilSesiBerjalan();
        tableModel.setDaftarSesi(daftarSesi);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMulai) {
            mulaiSesi();
        } else if (e.getSource() == btnAkhiri) {
            akhiriSesi();
        } else if (e.getSource() == btnMuatUlang) {
            muatData();
        }
    }
}
