package knet.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import knet.entity.Member;

public class MemberRepository {

    private Connection koneksi;

    public MemberRepository() {
        this.koneksi = Koneksi.getKoneksi();
    }

    public boolean simpanData(Member member) {
        String sql = "INSERT INTO member (nama, noHp, tanggalDaftar) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, member.getNama());
            ps.setString(2, member.getNoHp());
            ps.setDate(3, member.getTanggalDaftar());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean ubahData(Member member) {
        String sql = "UPDATE member SET nama = ?, noHp = ? WHERE idMember = ?";

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, member.getNama());
            ps.setString(2, member.getNoHp());
            ps.setInt(3, member.getIdMember());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusData(int idMember) {
        String sql = "DELETE FROM member WHERE idMember = ?";

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, idMember);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Member> ambilSemuaData() {
        String sql = "SELECT * FROM member ORDER BY idMember";
        List<Member> daftarMember = new ArrayList<Member>();

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Member member = buatMember(rs);
                daftarMember.add(member);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarMember;
    }

    public List<Member> cariData(String kataKunci) {
        String sql = "SELECT * FROM member WHERE nama LIKE ? OR noHp LIKE ? ORDER BY idMember";
        List<Member> daftarMember = new ArrayList<Member>();

        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, "%" + kataKunci + "%");
            ps.setString(2, "%" + kataKunci + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Member member = buatMember(rs);
                daftarMember.add(member);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarMember;
    }

    private Member buatMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setIdMember(rs.getInt("idMember"));
        member.setNama(rs.getString("nama"));
        member.setNoHp(rs.getString("noHp"));
        member.setTanggalDaftar(rs.getDate("tanggalDaftar"));
        return member;
    }
}
