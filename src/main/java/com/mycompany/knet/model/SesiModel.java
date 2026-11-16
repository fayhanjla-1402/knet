package com.mycompany.knet.model;

import java.sql.Timestamp;

public class SesiModel {

    private int idSesi;
    private int idMember;
    private String namaMember;
    private int idKomputer;
    private String namaKomputer;
    private Timestamp jamMulai;
    private Timestamp jamSelesai;
    private int totalBiaya;

    public SesiModel() {
    }

    public SesiModel(int idSesi, int idMember, String namaMember, int idKomputer, String namaKomputer, Timestamp jamMulai, Timestamp jamSelesai, int totalBiaya) {
        this.idSesi = idSesi;
        this.idMember = idMember;
        this.namaMember = namaMember;
        this.idKomputer = idKomputer;
        this.namaKomputer = namaKomputer;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.totalBiaya = totalBiaya;
    }

    public int getIdSesi() {
        return idSesi;
    }

    public int getIdMember() {
        return idMember;
    }

    public String getNamaMember() {
        return namaMember;
    }

    public int getIdKomputer() {
        return idKomputer;
    }

    public String getNamaKomputer() {
        return namaKomputer;
    }

    public Timestamp getJamMulai() {
        return jamMulai;
    }

    public Timestamp getJamSelesai() {
        return jamSelesai;
    }

    public int getTotalBiaya() {
        return totalBiaya;
    }

    public void setIdSesi(int idSesi) {
        this.idSesi = idSesi;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public void setNamaMember(String namaMember) {
        this.namaMember = namaMember;
    }

    public void setIdKomputer(int idKomputer) {
        this.idKomputer = idKomputer;
    }

    public void setNamaKomputer(String namaKomputer) {
        this.namaKomputer = namaKomputer;
    }

    public void setJamMulai(Timestamp jamMulai) {
        this.jamMulai = jamMulai;
    }

    public void setJamSelesai(Timestamp jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public void setTotalBiaya(int totalBiaya) {
        this.totalBiaya = totalBiaya;
    }
}