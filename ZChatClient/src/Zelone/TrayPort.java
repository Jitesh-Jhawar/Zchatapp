/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Zelone;

import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class TrayPort {

    private final String location = "LOG.bakchod";

    public static void main(String args[]) {
        TrayPort trayPort = new TrayPort();
    }

    String trayport = "0";

    public TrayPort() {

        chk:
        {
            //Check the SystemTray support
            if (!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
            /*final PopupMenu popup=new PopupMenu();*/
            final TrayIcon trayIcon
                    = new TrayIcon(createImage("/Zelone/tray.png", "Its me tray icon"));
            final SystemTray tray = SystemTray.getSystemTray();
            trayIcon.setImageAutoSize(true);

            try {
                createPopup2n(trayIcon, tray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    Menu Connected;

    private void createPopup2n(TrayIcon trayIcon, SystemTray tray) throws Exception {
        PopupMenu devn;
        trayIcon.setToolTip("Chat Server");

        Connected = new Menu("Connected People");
        MenuItem Exit = new MenuItem("Exit");
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);

                System.exit(0);
            }
        });
        MenuItem IP = new MenuItem("IP:" + getIP());
        MenuItem credits = new MenuItem("Credits");
        credits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Credits c = new Credits();
                c.setVisible(true);
            }
        });
        MenuItem slog = new MenuItem("Show log");
        slog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowLOG ss = new ShowLOG();
                ss.setVisible(true);
            }
        });
        CheckboxMenuItem dev = new CheckboxMenuItem("Developer Mode");

        Object[] gn = new Object[]{dev, "s", IP, Connected, "s", Exit, credits};
        devn = setMenu(gn);
        dev.setState(false);
        trayIcon.setPopupMenu(devn);
        dev.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED) {
                    try {
                        createPopup2p(trayIcon, tray);
                        setClients(threads);
                    } catch (Exception ex) {

                    }
                } else {
                    try {
                        createPopup2n(trayIcon, tray);
                        setClients(threads);
                    } catch (Exception ex) {

                    }
                }
            }
        });

        try {
            tray.add(trayIcon);
        } catch (Exception e) {
            System.out.println("TrayIcon could not be added.");

        }

    }

    private void createPopup2p(TrayIcon trayIcon, SystemTray tray) throws Exception {
        PopupMenu devp;
        trayIcon.setToolTip("Chat Server");

        Connected = new Menu("Connected People");
        MenuItem Exit = new MenuItem("Exit");
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);

                System.exit(0);
            }
        });
        MenuItem IP = new MenuItem("IP:" + getIP());
        MenuItem credits = new MenuItem("Credits");
        credits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Credits c = new Credits();
                c.setVisible(true);
            }
        });
        MenuItem slog = new MenuItem("Show log");
        slog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowLOG ss = new ShowLOG();
                ss.setVisible(true);
            }
        });
        CheckboxMenuItem dev = new CheckboxMenuItem("Developer Mode");

        Object[] gp = new Object[]{dev, "s", slog, "s", IP, Connected, "s", Exit, credits};
        devp = setMenu(gp);

        dev.setState(true);
        trayIcon.setPopupMenu(devp);
        dev.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED) {
                    try {
                        createPopup2p(trayIcon, tray);
                        setClients(threads);
                    } catch (Exception ex) {

                    }
                } else {
                    try {
                        createPopup2n(trayIcon, tray);
                        setClients(threads);
                    } catch (Exception ex) {

                    }
                }
            }
        });

        try {
            tray.add(trayIcon);
        } catch (Exception e) {
            System.out.println("TrayIcon could not be added.");

        }

    }

    protected static Image createImage(String path, String description) {
        URL imageURL = TrayPort.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    private static PopupMenu setMenu(Object[] menuitem) {
        PopupMenu popup = new PopupMenu();

        for (Object r : menuitem) {
            if (r.equals("s")) {
                popup.addSeparator();
            } else {
                popup.add((MenuItem) r);
            }
        }
        return popup;
    }

    private clientThread threads[];

    void setClients(clientThread[] threads) {
        this.threads = threads;
        Connected.removeAll();
        for (clientThread th : threads) {
            if (th != null) {
                addChats(th);
            }
        }

    }

    private void addChats(clientThread thread) {
        Menu chat = new Menu("" + thread.clientName);
        MenuItem kick = new MenuItem("Kick OUT");
        kick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int op = JOptionPane.showConfirmDialog(null, "Do u really want to throw " + thread.getName() + " to leave the chat room?", "Confirm Kick OUT", JOptionPane.YES_NO_OPTION);
                    if (op == JOptionPane.YES_OPTION) {
                        thread.Stop();
                        JOptionPane.showMessageDialog(null, thread.getName() + " has been kicked out of the chat room.");

                    } else if (op == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(null, thread.getName() + " has not been kicked out of the chat room.");
                    }
                } catch (Exception ex) {

                }
            }
        });
        chat.add(kick);
        Connected.add(chat);
    }

    private String getIP() throws Exception {
        String ip = InetAddress.getLocalHost().getHostAddress();
        return ip;
    }

}
