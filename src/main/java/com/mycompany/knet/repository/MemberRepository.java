package com.mycompany.knet.repository;

import com.mycompany.knet.model.MemberModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
    public List<MemberModel> getAllMembers() {
        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM member ORDER BY idMember");
                ResultSet rs = stmt.executeQuery()
        ) {
            return bacaDaftarMember(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public List<MemberModel> searchMembers(String keyword) {
        String sql = "SELECT * FROM member WHERE nama LIKE ? OR noHp LIKE ? ORDER BY idMember";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();
            List<MemberModel> members = bacaDaftarMember(rs);
            rs.close();

            return members;
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public void addMember(MemberModel member) {
        String sql = "INSERT INTO member (nama, noHp, tanggalDaftar) VALUES (?, ?, CURDATE())";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, member.getNama());
            stmt.setString(2, member.getNoHp());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal menyimpan data member!");
        }
    }

    public void updateMember(MemberModel member) {
        String sql = "UPDATE member SET nama = ?, noHp = ? WHERE idMember = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, member.getNama());
            stmt.setString(2, member.getNoHp());
            stmt.setInt(3, member.getIdMember());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal mengubah data member!");
        }
    }

    public void deleteMember(int idMember) {
        String sql = "DELETE FROM member WHERE idMember = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, idMember);
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Gagal menghapus member, member ini sudah tercatat di data sesi!");
        }
    }

    private List<MemberModel> bacaDaftarMember(ResultSet rs) throws SQLException {
        List<MemberModel> members = new ArrayList<>();

        while (rs.next()) {
            int idMember = rs.getInt("idMember");
            String nama = rs.getString("nama");
            String noHp = rs.getString("noHp");
            java.sql.Date tanggalDaftar = rs.getDate("tanggalDaftar");

            MemberModel memberModel = new MemberModel(idMember, nama, noHp, tanggalDaftar);
            members.add(memberModel);
        }

        return members;
    }
}
