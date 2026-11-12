/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.knet.model;

import java.sql.Timestamp;

/**
 *
 * @author Vivob
 */
public class SesiModel {
    private int idSesi;
    private int idMember;
    private int idKomputer;
    private Timestamp jamMulai;
    private Timestamp jamSelesai;
    private int totalBiaya;
    private String namaMember;
    private String namaKomputer;
 
    public SesiModel() {
    }
 
    public SesiModel(int idSesi, int idMember, int idKomputer, Timestamp jamMulai, Timestamp jamSelesai, int totalBiaya) {
        this.idSesi = idSesi;
        this.idMember = idMember;
        this.idKomputer = idKomputer;
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
 
    public int getIdKomputer() {
        return idKomputer;
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
 
    public String getNamaMember() {
        return namaMember;
    }
 
    public String getNamaKomputer() {
        return namaKomputer;
    }
 
    public void setIdSesi(int idSesi) {
        this.idSesi = idSesi;
    }
 
    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }
 
    public void setIdKomputer(int idKomputer) {
        this.idKomputer = idKomputer;
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
 
    public void setNamaMember(String namaMember) {
        this.namaMember = namaMember;
    }
 
    public void setNamaKomputer(String namaKomputer) {
        this.namaKomputer = namaKomputer;
    }
    
}
