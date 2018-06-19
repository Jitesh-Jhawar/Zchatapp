/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Zelone;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author mohammed
 */
// For every client's connection we call this class
public class clientThread extends Thread {

    public String clientName = null;
    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private final LOG Log;
    private TrayPort tray;

    public clientThread(Socket clientSocket, clientThread[] threads, String location, TrayPort tray) {
        System.out.println("ClientThread ");
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        this.Log = new LOG(location);
        this.tray = tray;
    }

    @Override
    public void run() {
        System.out.println("ClientThread run ");
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
            /*
       * Create input and output streams for this client.
             */
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            String name;
            while (true) {
                os.println("Enter your name.");
                name = is.readLine().trim();

                if (name.indexOf('@') == -1 || name.indexOf(' ') == -1) {
                    Log.saveName(name);
                    break;
                } else {
                    os.println("The name should not contain '@',' ' character.");
                }
            }
            try {
                tray.setClients(this.threads);
            } catch (Exception e) {

            }
            /* Welcome the new the client. */
            os.println("Welcome " + name
                    + " to our chat room.\nTo leave enter /quit in a new line.\n And for personal messages please type @<name of reciever as per chatgroup><space><message> in a new line \n IF ANY ERROR OCCURS CONTACT \nZELONE  GMAIL:zeloneapp@gmail.com ");
            synchronized (this) {
                System.out.println("ClientThread run synchronized1 ");
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] == this) {
                        clientName = "@" + name;
                        break;
                    }
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this) {
                        threads[i].os.println("*** A new user " + name
                                + " entered the chat room !!! ***");
                    }
                }
            }
            /* Start the conversation. */
            while (true) {
                String line = is.readLine();
                System.out.print("PART OF LINE::" + line);
                Log.saveLine(name, line + "||||");
                

                if (line.startsWith("/quit")) {

                    break;
                }
                /* If the message is private sent it to the given client. */
                if (line.startsWith("#")) {
                    String[] words = line.split("\\s", 2);
                    if (words.length > 1 && words[1] != null) {
                        words[1] = words[1].trim();
                        if (!words[1].isEmpty()) {
                            synchronized (this) {
                                System.out.println("ClientThread run synchronized1 ");
                               Set set= new Set();       
                               System.out.println(words[1]+"NAME:"+name);

                               set.doit(name,words[1]);
                            }
                        }
                    }
                } else if (line.startsWith("@")) {
                    String[] words = line.split("\\s", 2);
                    if (words.length > 1 && words[1] != null) {
                        words[1] = words[1].trim();
                        if (!words[1].isEmpty()) {
                            synchronized (this) {
                                System.out.println("ClientThread run synchronized2 ");
                                for (int i = 0; i < maxClientsCount; i++) {
                                    if (threads[i] != null && threads[i] != this
                                            && threads[i].clientName != null
                                            && threads[i].clientName.equals(words[0])) {
                                        threads[i].os.println("From:>" + name + "> " + words[1]);
                                        /*
                     * Echo this message to let the client know the private
                     * message was sent.
                                         */
                                        this.os.println("To:<" + words[0] + "< " + words[1]);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    /* The message is public, broadcast it to all other clients. */
                    synchronized (this) {
                        System.out.println("ClientThread run synchronized3 ");
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null) {
                                threads[i].os.println("<" + name + "> " + line);
                            }
                        }
                    }
                }
            }
            Stop();
        } catch (IOException e) {
        }
    }

    void Stop() throws IOException {
        clientThread[] threads = this.threads;
        String name = clientName;
        synchronized (this) {
            System.out.println("ClientThread run synchronized4 ");
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this
                        && threads[i].clientName != null) {
                    threads[i].os.println("*** The user " + name
                            + " is leaving the chat room !!! ***");
                }
            }
        }
        os.println("*** Bye " + name + " ***");

        /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
         */
        synchronized (this) {
            System.out.println("ClientThread run synchronized5 ");
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
        }
        /*
       * Close the output stream, close the input stream, close the socket.
         */
        try {
            tray.setClients(this.threads);
        } catch (Exception e) {

        }
        is.close();
        os.close();
        clientSocket.close();

    }
}
