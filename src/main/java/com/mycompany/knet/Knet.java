package com.mycompany.knet;

import com.mycompany.knet.view.FormMenuUtama;
import javax.swing.SwingUtilities;

public class Knet {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FormMenuUtama.main(args);
            }
        });
    }
}
