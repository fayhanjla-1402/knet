/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.knet.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Vivob
 */
class Koneksi {
    private static String url = "jdbc:mysql://localhost:3306/db_knet";
    private static String user = "root";
    private static String password = "";
    private static Connection koneksi;
 
    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                koneksi = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
 
        return koneksi;
    }
}
