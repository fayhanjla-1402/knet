package com.mycompany.knet.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.knet.entity.Sesi;

public class SesiRepository {

    private Connection koneksi;

    public SesiRepository() {
        this.koneksi = Koneksi.getKoneksi();
    }

    // Status komputer diubah otomatis oleh trigger trgSesiMulai.
    // Trigger trgSesiCekKomputer menolak sesi baru jika komputer masih Dipakai.
    public boolean mulaiSesi(int idMember, int idKomputer) {
        String sql = "INSERT INTO sesi (idMember, idKomputer, jamMulai) VALUES (?, ?, NOW())";

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, idMember);
            ps.setInt(2, idKomputer);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Jam selesai, total biaya, dan status komputer dihitung di database
    // lewat stored procedure spAkhiriSesi dan trigger trgSesiSelesai.
    // Mengembalikan total biaya, atau -1 jika gagal.
    public int akhiriSesi(int idSesi) {
        int totalBiaya = -1;

        try {
            CallableStatement cs = koneksi.prepareCall("{CALL spAkhiriSesi(?, ?)}");
            cs.setInt(1, idSesi);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();
            totalBiaya = cs.getInt(2);
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalBiaya;
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
