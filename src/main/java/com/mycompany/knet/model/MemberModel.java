/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.knet.model;

import java.sql.Date;

/**
 *
 * @author Vivob
 */
public class MemberModel {
    private int idMember;
    private String nama;
    private String noHp;
    private Date tanggalDaftar;

    public MemberModel() {
        this.idMember = 0;
        this.nama = "";
        this.noHp = "";
        this.tanggalDaftar = null;
    }

    public MemberModel(int idMember, String nama, String noHp, Date tanggalDaftar) {
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
    
    public void setMemberModel(int idMember, String nama, String noHp) {
        this.idMember = idMember;
        this.nama = nama;
        this.noHp = noHp;
        this.tanggalDaftar = tanggalDaftar;
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
