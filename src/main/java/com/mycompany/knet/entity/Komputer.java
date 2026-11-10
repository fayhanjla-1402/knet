/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.knet.entity;

/**
 *
 * @author Vivob
 */
public class Komputer {
    private int idKomputer;
    private String namaKomputer;
    private int tarifPerJam;
    private String status;

    public Komputer() {
    }

    public Komputer(int idKomputer, String namaKomputer, int tarifPerJam, String status) {
        this.idKomputer = idKomputer;
        this.namaKomputer = namaKomputer;
        this.tarifPerJam = tarifPerJam;
        this.status = status;
    }

    public int getIdKomputer() {
        return idKomputer;
    }

    public String getNamaKomputer() {
        return namaKomputer;
    }

    public int getTarifPerJam() {
        return tarifPerJam;
    }

    public String getStatus() {
        return status;
    }

    public void setIdKomputer(int idKomputer) {
        this.idKomputer = idKomputer;
    }

    public void setNamaKomputer(String namaKomputer) {
        this.namaKomputer = namaKomputer;
    }

    public void setTarifPerJam(int tarifPerJam) {
        this.tarifPerJam = tarifPerJam;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}