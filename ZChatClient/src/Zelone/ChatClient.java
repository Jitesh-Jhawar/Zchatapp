package Zelone;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.management.timer.Timer;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Class to manage Client chat Box.
public class ChatClient {

    /**
     * Chat client access
     */
    static class ChatAccess extends Observable {

        private Socket socket;
        private OutputStream outputStream;

        @Override
        public void notifyObservers(Object arg) {
            System.out.println("ChatAccess notifyObservers ");
            super.setChanged();
            super.notifyObservers(arg);
        }

        /**
         * Create socket, and receiving thread
         */
        public ChatAccess(String server, int port) throws IOException {
            System.out.println("ChatAccess ");
            socket = new Socket(server, port);
            outputStream = socket.getOutputStream();

            Thread receivingThread = new Thread() {
                @Override
                public void run() {
                    System.out.println("ChatAccess recivingThread run ");
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            notifyObservers(line);
                        }
                    } catch (IOException ex) {
                        notifyObservers(ex);
                    }
                }
            };
            receivingThread.start();
        }

        private static final String CRLF = "\r\n"; // newline

        /**
         * Send a line of text
         */
        public void send(String text) {
            System.out.println("ChatAccess send ");
            try {
                outputStream.write((text + CRLF).getBytes());
                outputStream.flush();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }

        /**
         * Close the socket
         */
        public void close() {
            System.out.println("ChatAccess close ");
            try {
                socket.close();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
    }

    /**
     * Chat client UI
     */
    static class ChatFrame extends JFrame implements Observer {

        private JTextArea textArea;
        private JTextField inputTextField;
        private JLabel sendButton;
        private ChatAccess chatAccess;

        public ChatFrame(ChatAccess chatAccess) {
            System.out.println("ChatFrame ");
            this.chatAccess = chatAccess;
            chatAccess.addObserver(this);
            buildGUI();
        }

        /**
         * Builds the user interface
         */
        private void buildGUI() {
            ActionListener sendListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("ChatFrame buildGUI ActionPerformed ");
                    String str = inputTextField.getText();
                    if (str != null && str.trim().length() > 0) {
                        chatAccess.send(str);
                    }
                    inputTextField.selectAll();
                    inputTextField.requestFocus();
                    inputTextField.setText("");
                    if (str.trim().startsWith("/quit")) {

                        try {
                            chatAccess.send("/quit");
                            chatAccess.close();
                            Thread.sleep(Timer.ONE_SECOND);

                        } catch (Exception exx) {
                        }
                        setVisible(false);
                        dispose();

                    }
                }
            };
            MouseAdapter exitListener = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        chatAccess.send("/quit");
                    } catch (Exception ex) {
                    }
                    chatAccess.close();
                    try {
                        Thread.sleep(Timer.ONE_SECOND);

                    } catch (Exception exx) {
                    }
                    setVisible(false);
                    dispose();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    BufferedImage im;
                    try {
                        im = ImageIO.read(getClass().getResource("/Zelone/Cclose.png"));
                        ((JLabel) e.getSource()).setIcon(new ImageIcon(im));
                    } catch (Exception ex) {

                    }

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    BufferedImage im;
                    try {
                        im = ImageIO.read(getClass().getResource("/Zelone/close.png"));
                        ((JLabel) e.getSource()).setIcon(new ImageIcon(im));
                    } catch (Exception ex) {

                    }
                    System.exit(0);
                }
            };
            textArea = new JTextArea(20, 50);
            textArea.setOpaque(false);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            buildGUI(sendListener, new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    BufferedImage im;
                    try {
                        im = ImageIO.read(getClass().getResource("/Zelone/Csend.png"));
                        ((JLabel) e.getSource()).setIcon(new ImageIcon(im));
                    } catch (Exception ex) {

                    }

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    BufferedImage im;
                    try {
                        im = ImageIO.read(getClass().getResource("/Zelone/send.png"));
                        ((JLabel) e.getSource()).setIcon(new ImageIcon(im));
                    } catch (Exception ex) {

                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("ChatFrame buildGUI ActionPerformed ");
                    String str = inputTextField.getText();
                    if (str != null && str.trim().length() > 0) {
                        chatAccess.send(str);
                    }
                    inputTextField.selectAll();
                    inputTextField.requestFocus();
                    inputTextField.setText("");
                    if (str.trim().startsWith("/quit")) {
                        chatAccess.close();
                        try {
                            Thread.sleep(Timer.ONE_SECOND);

                        } catch (Exception exx) {
                        }
                        setVisible(false);
                        dispose();
                    }
                }
            }, exitListener);
        }

        private void BackgroundMouseDragged(java.awt.event.MouseEvent evt) {
            int x = evt.getXOnScreen();
            int y = evt.getYOnScreen();

            this.setLocation(x - xMouse, y - yMouse);
        }
        int xMouse;
        int yMouse;

        private void BackgroundMousePressed(java.awt.event.MouseEvent evt) {
            xMouse = evt.getX();
            yMouse = evt.getY();
        }

        private void buildGUI(ActionListener send, MouseListener send2, MouseAdapter exitListen) {

            System.out.println("ChatFrame buildGUI ");
            setUndecorated(true);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            JLabel back = new JLabel();
            BufferedImage im;
            try {
                im = ImageIO.read(getClass().getResource("/Zelone/background.png"));
                System.out.println("IMAGE RECIEVED");
            } catch (Exception e) {
                im = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
                e.printStackTrace();
            }
            setSize(im.getWidth(), im.getHeight());

            back.setSize(im.getWidth(), im.getHeight());
            //back.setBounds(0, 0, back.getWidth(), back.getHeight());
            back.setIcon(new javax.swing.ImageIcon(im));
            JScrollPane jsc = new JScrollPane(textArea);
            jsc.setOpaque(false);
            jsc.setBounds(23, 66, 590, 354);
            add(jsc);
            back.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    BackgroundMouseDragged(e);
                }
            });
            back.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    BackgroundMousePressed(e);
                }

            });

            inputTextField = new JTextField();
            sendButton = new JLabel();
            inputTextField.setBorder(null);
            sendButton.setBounds(496, 434, 117, 32);
            inputTextField.setBounds(23, 434, 446, 32);
            add(inputTextField);
            add(sendButton);
            JLabel exitBtn = new JLabel();
            exitBtn.setBounds(602, 15, 24, 24);
            exitBtn.addMouseListener(exitListen);
            add(exitBtn);
            add(back);
            // Action for the inputTextField and the goButton

            inputTextField.addActionListener(send);
            sendButton.addMouseListener(send2);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("ChatFrame buildGUI WindowClosing ");
                    try {
                        chatAccess.send("/quit");
                    } catch (Exception ex) {
                    }
                    chatAccess.close();
                }
            });
            setVisible(true);
        }

        /**
         * Updates the UI depending on the Object argument
         */
        public void update(Observable o, Object arg) {
            System.out.println("ChatFrame update " + arg);
            final Object finalArg = arg;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("ChatFrame update run " + finalArg);
                    textArea.append(finalArg.toString());
                    try {
                        Toolkit.getDefaultToolkit().beep();
                        setAlwaysOnTop(true);
                        setAlwaysOnTop(false);
                    } catch (Exception exx) {
                    }
                    textArea.append("\n");
                }
            });
        }
    }

    public static void main(String[] args) {
        System.out.println("ChatClient main ");
        String server = args[0];
        int port = 2222;
        ChatAccess access = null;
        try {
            access = new ChatAccess(server, port);
        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
            ex.printStackTrace();
            System.exit(0);
        }
        JFrame frame = new ChatFrame(access);
        frame.setTitle("MyChatApp - connected to " + server + ":" + port);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
