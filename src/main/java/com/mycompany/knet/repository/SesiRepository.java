package knet.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import knet.entity.Sesi;

public class SesiRepository {

    private Connection koneksi;
    private KomputerRepository komputerRepository;

    public SesiRepository() {
        this.koneksi = Koneksi.getKoneksi();
        this.komputerRepository = new KomputerRepository();
    }

    public boolean mulaiSesi(int idMember, int idKomputer) {
        String sql = "INSERT INTO sesi (idMember, idKomputer, jamMulai) VALUES (?, ?, ?)";

        try {
            Timestamp sekarang = new Timestamp(System.currentTimeMillis());
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, idMember);
            ps.setInt(2, idKomputer);
            ps.setTimestamp(3, sekarang);
            ps.executeUpdate();
            ps.close();

            komputerRepository.ubahStatus(idKomputer, "Dipakai");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int akhiriSesi(int idSesi) {
        String sqlAmbil = "SELECT sesi.idKomputer, sesi.jamMulai, komputer.tarifPerJam "
                + "FROM sesi JOIN komputer ON sesi.idKomputer = komputer.idKomputer "
                + "WHERE sesi.idSesi = ? AND sesi.jamSelesai IS NULL";
        String sqlUbah = "UPDATE sesi SET jamSelesai = ?, totalBiaya = ? WHERE idSesi = ?";
        int totalBiaya = -1;

        try {
            PreparedStatement psAmbil = koneksi.prepareStatement(sqlAmbil);
            psAmbil.setInt(1, idSesi);
            ResultSet rs = psAmbil.executeQuery();

            if (rs.next()) {
                int idKomputer = rs.getInt("idKomputer");
                Timestamp jamMulai = rs.getTimestamp("jamMulai");
                int tarifPerJam = rs.getInt("tarifPerJam");
                Timestamp jamSelesai = new Timestamp(System.currentTimeMillis());

                totalBiaya = hitungTotalBiaya(jamMulai, jamSelesai, tarifPerJam);

                PreparedStatement psUbah = koneksi.prepareStatement(sqlUbah);
                psUbah.setTimestamp(1, jamSelesai);
                psUbah.setInt(2, totalBiaya);
                psUbah.setInt(3, idSesi);
                psUbah.executeUpdate();
                psUbah.close();

                komputerRepository.ubahStatus(idKomputer, "Tersedia");
            }

            rs.close();
            psAmbil.close();
        } catch (SQLException e) {
            e.printStackTrace();
            totalBiaya = -1;
        }

        return totalBiaya;
    }

    public int hitungTotalBiaya(Timestamp jamMulai, Timestamp jamSelesai, int tarifPerJam) {
        long selisihMilidetik = jamSelesai.getTime() - jamMulai.getTime();
        long totalMenit = selisihMilidetik / 60000;
        long lamaJam = totalMenit / 60;

        if (totalMenit % 60 > 0) {
            lamaJam = lamaJam + 1;
        }

        if (lamaJam < 1) {
            lamaJam = 1;
        }

        return (int) (lamaJam * tarifPerJam);
    }

    public List<Sesi> ambilSesiBerjalan() {
        String sql = "SELECT * FROM vRiwayatSesi WHERE jamSelesai IS NULL ORDER BY idSesi";
        List<Sesi> daftarSesi = new ArrayList<Sesi>();

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            daftarSesi = bacaDaftarSesi(ps);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarSesi;
    }

    public List<Sesi> ambilRiwayat() {
        String sql = "SELECT * FROM vRiwayatSesi ORDER BY idSesi DESC";
        List<Sesi> daftarSesi = new ArrayList<Sesi>();

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            daftarSesi = bacaDaftarSesi(ps);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarSesi;
    }

    public List<Sesi> ambilRiwayatPerTanggal(Date tanggal) {
        String sql = "SELECT * FROM vRiwayatSesi WHERE DATE(jamMulai) = ? ORDER BY idSesi DESC";
        List<Sesi> daftarSesi = new ArrayList<Sesi>();

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setDate(1, tanggal);
            daftarSesi = bacaDaftarSesi(ps);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarSesi;
    }

    public int ambilPendapatanPerTanggal(Date tanggal) {
        String sql = "SELECT SUM(totalBiaya) FROM sesi WHERE DATE(jamMulai) = ? AND jamSelesai IS NOT NULL";
        int totalPendapatan = 0;

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setDate(1, tanggal);
            totalPendapatan = bacaAngka(ps);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPendapatan;
    }

    public int ambilPendapatanTotal() {
        String sql = "SELECT SUM(totalBiaya) FROM sesi WHERE jamSelesai IS NOT NULL";
        int totalPendapatan = 0;

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            totalPendapatan = bacaAngka(ps);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPendapatan;
    }

    private List<Sesi> bacaDaftarSesi(PreparedStatement ps) throws SQLException {
        List<Sesi> daftarSesi = new ArrayList<Sesi>();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Sesi sesi = buatSesi(rs);
            daftarSesi.add(sesi);
        }

        rs.close();
        return daftarSesi;
    }

    private int bacaAngka(PreparedStatement ps) throws SQLException {
        int angka = 0;
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            angka = rs.getInt(1);
        }

        rs.close();
        return angka;
    }

    private Sesi buatSesi(ResultSet rs) throws SQLException {
        Sesi sesi = new Sesi();
        sesi.setIdSesi(rs.getInt("idSesi"));
        sesi.setIdMember(rs.getInt("idMember"));
        sesi.setIdKomputer(rs.getInt("idKomputer"));
        sesi.setNamaMember(rs.getString("namaMember"));
        sesi.setNamaKomputer(rs.getString("namaKomputer"));
        sesi.setJamMulai(rs.getTimestamp("jamMulai"));
        sesi.setJamSelesai(rs.getTimestamp("jamSelesai"));
        sesi.setTotalBiaya(rs.getInt("totalBiaya"));
        return sesi;
    }
}
