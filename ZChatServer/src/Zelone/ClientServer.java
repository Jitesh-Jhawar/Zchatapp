package Zelone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

//Class to precise who is connected : Client or Server
public class ClientServer {

    public static void main(String[] args) {
        System.out.println("ChatServer main ");
        File f = new File("run.cmd");
        if (!f.exists()) {
            try {
                new FileOutputStream(f).write("\njava -jar ZChatServer.jar\n".getBytes());
            } catch (Exception ex) {

            }
        }
        // Object[] selectioValues = {"Server", "Client"};
        // String initialSection = "Server";
        String selection = "Server"; //JOptionPane.showInputDialog(null, "Login as : ", "MyChatApp", JOptionPane.QUESTION_MESSAGE, null, selectioValues, initialSection);
        //  if (selection == null) {
        //  System.exit(0);
        //} else
        if (selection.equals("Server")) {
            String[] arguments = new String[]{};
            new MultiThreadChatServerSync().main(arguments);
        } else if (selection.equals("Client")) {
            String IPServer = JOptionPane.showInputDialog("Enter the Server ip adress");
            String[] arguments = new String[]{IPServer};
            new ChatClient().main(arguments);
        }

    }

}
