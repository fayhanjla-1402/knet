package com.mycompany.knet.entity;

import java.sql.Date;

public class Member {
    private int idMember;
    private String nama;
    private String noHp;
    private Date tanggalDaftar;

    public Member() {
    }

    public Member(int idMember, String nama, String noHp, Date tanggalDaftar) {
        this.idMember = idMember;
        this.nama = nama;
        this.noHp = noHp;
        this.tanggalDaftar = tanggalDaftar;
    }

    public int getIdMember() {
        return idMember;
    }

    public String getNama() {
        return nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public Date getTanggalDaftar() {
        return tanggalDaftar;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public void setTanggalDaftar(Date tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }
}
