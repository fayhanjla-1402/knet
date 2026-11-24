package com.mycompany.knet.repository;

import com.mycompany.knet.model.KomputerModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
        String cekSesi = "SELECT COUNT(*) FROM sesi "
                       + "WHERE idKomputer = ? AND jamSelesai IS NULL";
        String update = "UPDATE komputer SET status = ? "
                      + "WHERE idKomputer = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement psCek = conn.prepareStatement(cekSesi)) {

            psCek.setInt(1, idKomputer);

            try (ResultSet rs = psCek.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0
                        && status.equals("Tersedia")) {

                    JOptionPane.showMessageDialog(null,"Komputer sedang digunakan dalam sesi!\n"
                        + "Status tetap Dipakai.");

                    return;
                }
            }

            try (PreparedStatement psUpdate = conn.prepareStatement(update)) {
                psUpdate.setString(1, status);
                psUpdate.setInt(2, idKomputer);
                psUpdate.executeUpdate();

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Gagal mengubah status: " + e.getMessage());
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
