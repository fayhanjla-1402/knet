/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.knet.model;

import com.mycompany.knet.entity.Sesi;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Vivob
 */
public class SesiTableModel extends AbstractTableModel {
    private List<Sesi> daftarSesi;
    private String[] namaKolom = {"ID Sesi", "Member", "Komputer", "Jam Mulai", "Jam Selesai", "Total Biaya"};
 
    public SesiTableModel() {
        this.daftarSesi = new ArrayList<Sesi>();
    }
 
    public SesiTableModel(List<Sesi> daftarSesi) {
        this.daftarSesi = daftarSesi;
    }
 
    public List<Sesi> getDaftarSesi() {
        return daftarSesi;
    }
 
    public Sesi getSesiAt(int baris) {
        return daftarSesi.get(baris);
    }
 
    public void setDaftarSesi(List<Sesi> daftarSesi) {
        this.daftarSesi = daftarSesi;
        fireTableDataChanged();
    }
 
    public void tambahSesi(Sesi sesi) {
        daftarSesi.add(sesi);
        fireTableDataChanged();
    }
 
    public void kosongkanData() {
        daftarSesi.clear();
        fireTableDataChanged();
    }
 
    @Override
    public int getRowCount() {
        return daftarSesi.size();
    }
 
    @Override
    public int getColumnCount() {
        return namaKolom.length;
    }
 
    @Override
    public String getColumnName(int kolom) {
        return namaKolom[kolom];
    }
 
    @Override
    public Object getValueAt(int baris, int kolom) {
        Sesi sesi = daftarSesi.get(baris);
 
        switch (kolom) {
            case 0:
                return sesi.getIdSesi();
            case 1:
                return sesi.getNamaMember();
            case 2:
                return sesi.getNamaKomputer();
            case 3:
                return sesi.getJamMulai();
            case 4:
                return sesi.getJamSelesai();
            case 5:
                return sesi.getTotalBiaya();
            default:
                return null;
        }
    }
}
