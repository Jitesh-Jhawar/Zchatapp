/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Zelone;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class LOG {

    private final String location;

    public LOG(String location) {
        this.location = location;
    }

    void saveName(String name) {
        new Thread() {
            @Override
            public void run() {
                BasicCodes bc = new BasicCodes();
                Date d = new Date();
                bc.AddToSave("\n" + name + " \t" + d.toString() + "\t *** A new user " + name + " entered the chat room !!! ***", location);
            }
        }.start();
    }

    void saveLine(String name, String line) {
        new Thread() {
            @Override
            public void run() {
                BasicCodes bc = new BasicCodes();
                Date d = new Date();
                bc.AddToSave("\n" + name + "\t" + d.toString() + "\t " + line + "", location);

            }
        }.start();
    }

    private class BasicCodes {

        private void AddToSave(String msg, String FileAddress) {
            Save(open(FileAddress) + "\n" + msg, FileAddress);

        }

        private String open(String path) {
            String cc;
            try {
                File f = new File(path);
                Scanner input = new Scanner(f);
                String kkkk = "";
                while (input.hasNextLine()) {
                    kkkk = kkkk + "\n" + (String) input.nextLine();
                }
                cc = kkkk;
            } catch (Exception e) {
                cc = "error";
            }
            return "" + cc;
        }

        private void Save(String msg, String FileAddress) {
            String cc;
            try {
                File f = new File(FileAddress);
                try (PrintWriter output = new PrintWriter(f)) {
                    output.print("" + msg);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR IN SAVING" + e);
            }
        }

    }

}
