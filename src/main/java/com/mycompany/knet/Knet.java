package com.mycompany.knet;

import com.mycompany.knet.view.FormMember;

public class Knet {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new FormMember().setVisible(true);
        });
    }
}
