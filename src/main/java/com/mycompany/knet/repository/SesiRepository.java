package com.mycompany.knet.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.knet.model.SesiModel;

public class SesiRepository {

    private String sqlDasar = "SELECT v.*, k.status AS statusKomputer FROM vRiwayatSesi v JOIN komputer k ON v.idKomputer = k.idKomputer ";

    public List<SesiModel> getAllSesi() {
        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlDasar + "ORDER BY v.idSesi DESC");
                ResultSet rs = stmt.executeQuery()
        ) {
            return bacaDaftarSesi(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public List<SesiModel> getSesiBerjalan() {
        String sql = sqlDasar + "WHERE v.jamSelesai IS NULL ORDER BY v.idSesi";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            return bacaDaftarSesi(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public List<SesiModel> getSesiByTanggal(Date tanggal) {
        String sql = sqlDasar + "WHERE DATE(v.jamMulai) = ? ORDER BY v.idSesi DESC";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setDate(1, tanggal);

            ResultSet rs = stmt.executeQuery();
            List<SesiModel> daftarSesi = bacaDaftarSesi(rs);
            rs.close();

            return daftarSesi;
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public int getTotalPendapatan() {
        String sql = "SELECT SUM(totalBiaya) FROM sesi WHERE jamSelesai IS NOT NULL";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int getPendapatanByTanggal(Date tanggal) {
        String sql = "SELECT SUM(totalBiaya) FROM sesi WHERE DATE(jamMulai) = ? AND jamSelesai IS NOT NULL";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setDate(1, tanggal);

            ResultSet rs = stmt.executeQuery();
            int totalPendapatan = 0;

            if (rs.next()) {
                totalPendapatan = rs.getInt(1);
            }

            rs.close();
            return totalPendapatan;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public void addSesi(SesiModel sesi) {
        String sql = "INSERT INTO sesi (idMember, idKomputer, jamMulai) VALUES (?, ?, NOW())";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, sesi.getIdMember());
            stmt.setInt(2, sesi.getIdKomputer());
            // trgSesiCekKomputer menolak jika komputer sedang dipakai,
            // trgSesiMulai mengubah status komputer menjadi Dipakai
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(
                    "Gagal memulai sesi: " + ex.getMessage()
            );
        }
    }

    public int finishSesi(int idSesi) {
        String sql = "{CALL spAkhiriSesi(?, ?)}";
        int totalBiaya = -1;

        try (
                Connection conn = DatabaseUtil.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)
        ) {
            stmt.setInt(1, idSesi);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            totalBiaya = stmt.getInt(2);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal mengakhiri sesi!");
        }

        if (totalBiaya < 0) {
            throw new RuntimeException("Sesi tidak ditemukan atau sudah selesai");
        }

        return totalBiaya;
    }

    private List<SesiModel> bacaDaftarSesi(ResultSet rs) throws SQLException {
        List<SesiModel> daftarSesi = new ArrayList<>();

        while (rs.next()) {
            int idSesi = rs.getInt("idSesi");
            int idMember = rs.getInt("idMember");
            String namaMember = rs.getString("namaMember");
            int idKomputer = rs.getInt("idKomputer");
            String namaKomputer = rs.getString("namaKomputer");
            String statusKomputer = rs.getString("statusKomputer");
            Timestamp jamMulai = rs.getTimestamp("jamMulai");
            Timestamp jamSelesai = rs.getTimestamp("jamSelesai");
            int totalBiaya = rs.getInt("totalBiaya");

            SesiModel sesiModel = new SesiModel(idSesi, idMember, namaMember, idKomputer, namaKomputer, statusKomputer, jamMulai, jamSelesai, totalBiaya);
            daftarSesi.add(sesiModel);
        }

        return daftarSesi;
    }
}