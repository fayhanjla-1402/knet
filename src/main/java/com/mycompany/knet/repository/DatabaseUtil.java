
package com.mycompany.knet.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static String url = "jdbc:mysql://localhost:3306/db_knet";
    private static String user = "root";
    private static String password = "";
    private static Connection koneksi;

    public static Connection getKoneksi() {
        try {
            if (koneksi == null || koneksi.isClosed()) {
                koneksi = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            System.err.println("Gagal terhubung ke database: " + e.getMessage());
        }

        return koneksi;
    }

    public static Connection getConnection() {
        return getKoneksi();
    }
}
