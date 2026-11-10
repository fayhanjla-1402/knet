/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.knet.repository;

/**
 *
 * @author Vivob
 */
import com.mycompany.knet.entity.Komputer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class KomputerRepository { 
    private Connection koneksi;
 
    public KomputerRepository() {
        this.koneksi = Koneksi.getKoneksi();
    }
 
    public boolean simpanData(Komputer komputer) {
        String sql = "INSERT INTO komputer (namaKomputer, tarifPerJam, status) VALUES (?, ?, ?)";
 
        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, komputer.getNamaKomputer());
            ps.setInt(2, komputer.getTarifPerJam());
            ps.setString(3, komputer.getStatus());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    public boolean ubahData(Komputer komputer) {
        String sql = "UPDATE komputer SET namaKomputer = ?, tarifPerJam = ?, status = ? WHERE idKomputer = ?";
 
        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, komputer.getNamaKomputer());
            ps.setInt(2, komputer.getTarifPerJam());
            ps.setString(3, komputer.getStatus());
            ps.setInt(4, komputer.getIdKomputer());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    public boolean hapusData(int idKomputer) {
        String sql = "DELETE FROM komputer WHERE idKomputer = ?";
 
        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, idKomputer);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    public boolean ubahStatus(int idKomputer, String status) {
        String sql = "UPDATE komputer SET status = ? WHERE idKomputer = ?";
 
        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, idKomputer);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    public List<Komputer> ambilSemuaData() {
        String sql = "SELECT * FROM komputer ORDER BY idKomputer";
        List<Komputer> daftarKomputer = new ArrayList<Komputer>();
 
        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                Komputer komputer = buatKomputer(rs);
                daftarKomputer.add(komputer);
            }
 
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return daftarKomputer;
    }
 
    public List<Komputer> ambilKomputerTersedia() {
        String sql = "SELECT * FROM komputer WHERE status = 'Tersedia' ORDER BY idKomputer";
        List<Komputer> daftarKomputer = new ArrayList<Komputer>();
 
        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                Komputer komputer = buatKomputer(rs);
                daftarKomputer.add(komputer);
            }
 
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return daftarKomputer;
    }
 
    public List<Komputer> cariData(String kataKunci) {
        String sql = "SELECT * FROM komputer WHERE namaKomputer LIKE ? ORDER BY idKomputer";
        List<Komputer> daftarKomputer = new ArrayList<Komputer>();
 
        try {
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, "%" + kataKunci + "%");
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                Komputer komputer = buatKomputer(rs);
                daftarKomputer.add(komputer);
            }
 
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return daftarKomputer;
    }
 
    private Komputer buatKomputer(ResultSet rs) throws SQLException {
        Komputer komputer = new Komputer();
        komputer.setIdKomputer(rs.getInt("idKomputer"));
        komputer.setNamaKomputer(rs.getString("namaKomputer"));
        komputer.setTarifPerJam(rs.getInt("tarifPerJam"));
        komputer.setStatus(rs.getString("status"));
        return komputer;
    }
}
 
