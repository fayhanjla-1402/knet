/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.knet.model;
import com.mycompany.knet.entity.Komputer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Vivob
 */
public class KomputerModel  extends AbstractTableModel {
    private List<Komputer> daftarKomputer;
    private String[] namaKolom = {"ID", "Nama Komputer", "Tarif per Jam", "Status"};
 
    public KomputerModel() {
        this.daftarKomputer = new ArrayList<Komputer>();
    }
 
    public KomputerModel(List<Komputer> daftarKomputer) {
        this.daftarKomputer = daftarKomputer;
    }
 
    public List<Komputer> getDaftarKomputer() {
        return daftarKomputer;
    }
 
    public Komputer getKomputerAt(int baris) {
        return daftarKomputer.get(baris);
    }
 
    public void setDaftarKomputer(List<Komputer> daftarKomputer) {
        this.daftarKomputer = daftarKomputer;
        fireTableDataChanged();
    }
 
    @Override
    public int getRowCount() {
        return daftarKomputer.size();
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
        Komputer komputer = daftarKomputer.get(baris);
 
        switch (kolom) {
            case 0:
                return komputer.getIdKomputer();
            case 1:
                return komputer.getNamaKomputer();
            case 2:
                return komputer.getTarifPerJam();
            case 3:
                return komputer.getStatus();
            default:
                return null;
        }
    }
}
