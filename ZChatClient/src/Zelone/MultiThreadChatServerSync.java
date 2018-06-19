package Zelone;

import java.io.PrintStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import javax.swing.JOptionPane;

/**
 *
 * @author mohammed
 */
// the Server class
public class MultiThreadChatServerSync {
    // The server socket.

    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    // This chat server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    public static void main(String args[]) {
        System.out.println("MultiThreadChatServer main ");

        // The default port number.
        int portNumber = 2222;
        if (args.length < 1) {
            String ip = "localhost";
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
            }
            System.out.println("Usage: java MultiThreadChatServerSync <portNumber>\n"
                    + "Now using port number=" + portNumber + "\n Now using IP Address= " + ip);

            new Thread() {
                @Override
                public void run() {
                    try {
                        JOptionPane.showMessageDialog(null, "THE IP ADDRESS IS :" + InetAddress.getLocalHost().getHostAddress());
                    } catch (Exception e) {

                    }
                }
            }.start();
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }

        /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
         */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
  
         */
        String location = "LOG.bakchod";
        TrayPort tray = new TrayPort();

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads, location, tray)).start();

                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
