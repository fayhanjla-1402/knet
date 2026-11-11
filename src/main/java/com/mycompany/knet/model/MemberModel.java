package knet.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import knet.entity.Member;

public class MemberModel extends AbstractTableModel {

    private List<Member> daftarMember;
    private String[] namaKolom = {"ID", "Nama", "No HP", "Tanggal Daftar"};

    public MemberModel() {
        this.daftarMember = new ArrayList<Member>();
    }

    public MemberModel(List<Member> daftarMember) {
        this.daftarMember = daftarMember;
    }

    public List<Member> getDaftarMember() {
        return daftarMember;
    }

    public Member getMemberAt(int baris) {
        return daftarMember.get(baris);
    }

    public void setDaftarMember(List<Member> daftarMember) {
        this.daftarMember = daftarMember;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return daftarMember.size();
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
        Member member = daftarMember.get(baris);

        switch (kolom) {
            case 0:
                return member.getIdMember();
            case 1:
                return member.getNama();
            case 2:
                return member.getNoHp();
            case 3:
                return member.getTanggalDaftar();
            default:
                return null;
        }
    }
}
