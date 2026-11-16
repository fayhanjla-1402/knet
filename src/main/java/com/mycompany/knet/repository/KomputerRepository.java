package com.mycompany.knet.repository;

import com.mycompany.knet.model.KomputerModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KomputerRepository {
    public List<KomputerModel> getAllKomputer() {
        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM komputer ORDER BY idKomputer");
                ResultSet rs = stmt.executeQuery()
        ) {
            return bacaDaftarKomputer(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public List<KomputerModel> getKomputerTersedia() {
        String sql = "SELECT * FROM komputer WHERE status = 'Tersedia' ORDER BY idKomputer";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            return bacaDaftarKomputer(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public List<KomputerModel> searchKomputer(String keyword) {
        String sql = "SELECT * FROM komputer WHERE namaKomputer LIKE ? ORDER BY idKomputer";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();
            List<KomputerModel> daftarKomputer = bacaDaftarKomputer(rs);
            rs.close();

            return daftarKomputer;
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public void addKomputer(KomputerModel komputer) {
        String sql = "INSERT INTO komputer (namaKomputer, tarifPerJam) VALUES (?, ?)";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, komputer.getNamaKomputer());
            stmt.setInt(2, komputer.getTarifPerJam());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal menyimpan data komputer, nama komputer mungkin sudah dipakai!");
        }
    }

    public void updateKomputer(KomputerModel komputer) {
        String sql = "UPDATE komputer SET namaKomputer = ?, tarifPerJam = ? WHERE idKomputer = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, komputer.getNamaKomputer());
            stmt.setInt(2, komputer.getTarifPerJam());
            stmt.setInt(3, komputer.getIdKomputer());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal mengubah data komputer, nama komputer mungkin sudah dipakai!");
        }
    }

    public void updateStatusKomputer(int idKomputer, String status) {
        String sqlCek = "SELECT COUNT(*) FROM sesi WHERE idKomputer = ? AND jamSelesai IS NULL";
        String sqlUbah = "UPDATE komputer SET status = ? WHERE idKomputer = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmtCek = conn.prepareStatement(sqlCek);
                PreparedStatement stmtUbah = conn.prepareStatement(sqlUbah)
        ) {
            stmtCek.setInt(1, idKomputer);
            ResultSet rs = stmtCek.executeQuery();
            int jumlahSesiBerjalan = 0;

            if (rs.next()) {
                jumlahSesiBerjalan = rs.getInt(1);
            }

            rs.close();

            if (jumlahSesiBerjalan > 0 && status.equals("Tersedia")) {
                throw new RuntimeException("Komputer ini masih punya sesi berjalan, akhiri sesinya dulu!");
            }

            stmtUbah.setString(1, status);
            stmtUbah.setInt(2, idKomputer);
            stmtUbah.executeUpdate();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal mengubah status komputer!");
        }
    }

    public void deleteKomputer(int idKomputer) {
        String sql = "DELETE FROM komputer WHERE idKomputer = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, idKomputer);
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal menghapus komputer, komputer ini sudah tercatat di data sesi!");
        }
    }

    private List<KomputerModel> bacaDaftarKomputer(ResultSet rs) throws SQLException {
        List<KomputerModel> daftarKomputer = new ArrayList<>();

        while (rs.next()) {
            int idKomputer = rs.getInt("idKomputer");
            String namaKomputer = rs.getString("namaKomputer");
            int tarifPerJam = rs.getInt("tarifPerJam");
            String status = rs.getString("status");

            KomputerModel komputerModel = new KomputerModel(idKomputer, namaKomputer, tarifPerJam, status);
            daftarKomputer.add(komputerModel);
        }

        return daftarKomputer;
    }
}
