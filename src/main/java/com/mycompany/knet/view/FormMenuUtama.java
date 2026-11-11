package knet.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FormMenuUtama extends JFrame implements ActionListener {

    private JButton btnMember;
    private JButton btnKomputer;
    private JButton btnSesi;
    private JButton btnLaporan;
    private JButton btnKeluar;

    public FormMenuUtama() {
        siapkanTampilan();
    }

    private void siapkanTampilan() {
        setTitle("Knet - Menu Utama");
        setSize(360, 380);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblJudul = new JLabel("KNET - Manajemen Warnet", SwingConstants.CENTER);
        lblJudul.setBounds(40, 20, 270, 30);
        add(lblJudul);

        btnMember = new JButton("Data Member");
        btnMember.setBounds(80, 70, 200, 40);
        btnMember.addActionListener(this);
        add(btnMember);

        btnKomputer = new JButton("Data Komputer");
        btnKomputer.setBounds(80, 120, 200, 40);
        btnKomputer.addActionListener(this);
        add(btnKomputer);

        btnSesi = new JButton("Sesi Pemakaian");
        btnSesi.setBounds(80, 170, 200, 40);
        btnSesi.addActionListener(this);
        add(btnSesi);

        btnLaporan = new JButton("Laporan");
        btnLaporan.setBounds(80, 220, 200, 40);
        btnLaporan.addActionListener(this);
        add(btnLaporan);

        btnKeluar = new JButton("Keluar");
        btnKeluar.setBounds(80, 270, 200, 40);
        btnKeluar.addActionListener(this);
        add(btnKeluar);
    }

    private void bukaFormMember() {
        FormMember formMember = new FormMember();
        formMember.setVisible(true);
    }

    private void bukaFormKomputer() {
        FormKomputer formKomputer = new FormKomputer();
        formKomputer.setVisible(true);
    }

    private void bukaFormSesi() {
        FormSesi formSesi = new FormSesi();
        formSesi.setVisible(true);
    }

    private void bukaFormLaporan() {
        FormLaporan formLaporan = new FormLaporan();
        formLaporan.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMember) {
            bukaFormMember();
        } else if (e.getSource() == btnKomputer) {
            bukaFormKomputer();
        } else if (e.getSource() == btnSesi) {
            bukaFormSesi();
        } else if (e.getSource() == btnLaporan) {
            bukaFormLaporan();
        } else if (e.getSource() == btnKeluar) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        FormMenuUtama formMenuUtama = new FormMenuUtama();
        formMenuUtama.setVisible(true);
    }
}
