package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {
    private JFrame jFrame;
    private JLabel jlNumber;
    private int num; // number of clients connected
    private ArrayList<File> files = new ArrayList<>();
    private JLabel jlSentFile;
    private JLabel jlDelFile;

    public ServerMain() throws IOException {
        initElements();
        makeFileList();

        ServerSocket serverSocket = new ServerSocket(2021); // Control Connection port
        ServerSocket serverDataSocket = new ServerSocket(2020); // Data Connection port

        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, this, serverDataSocket, ++num);
            Thread thread = new Thread(serverThread);
            thread.start();
        }
    }

    private void makeFileList() {
        File directory = new File("./ServerFiles");
        File[] matchingFiles = directory.listFiles((directory1, name) -> name.endsWith("txt"));

        for (int i = 0; i < matchingFiles.length; i++) {
            files.add(matchingFiles[i]);
        }
    }

    private void initElements() {
        jFrame = new JFrame("FTP Server");
        jFrame.setSize(600, 600);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dim.width / 2 - jFrame.getSize().width / 2;
        int y = dim.height / 2 - jFrame.getSize().height / 2;
        jFrame.setLocation(x - 500, y);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jlTitle = new JLabel("Sabir File Server");
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpStatus = new JPanel();
        jpStatus.setLayout(new FlowLayout(FlowLayout.LEFT,40,0));
        jpStatus.setBorder(new EmptyBorder(50, 0, 0,0));

        JLabel jlServerStatus = new JLabel("Server status: ");
        jlServerStatus.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel jlRunning = new JLabel("RUNNING");
        jlRunning.setFont(new Font("Arial", Font.BOLD, 20));
        jlRunning.setForeground(new Color(0, 204, 0));

        jpStatus.add(jlServerStatus);
        jpStatus.add(jlRunning);

        JPanel jpNumOfClients = new JPanel();
        jpNumOfClients.setLayout(new FlowLayout(FlowLayout.LEFT,40,0));
        jpNumOfClients.setBorder(new EmptyBorder(0, 0, 10,0));

        JLabel jlNumOfClients = new JLabel("Number of clients connected: ");
        jlNumOfClients.setFont(new Font("Arial", Font.BOLD, 20));

        jlNumber= new JLabel("0");
        jlNumber.setFont(new Font("Arial", Font.BOLD, 20));

        jpNumOfClients.add(jlNumOfClients);
        jpNumOfClients.add(jlNumber);

        JPanel jpSent = new JPanel();
        jpSent.setLayout(new FlowLayout(FlowLayout.LEFT,40,0));
        jpSent.setBorder(new EmptyBorder(50, 0, 0,0));

        JLabel jlSentText = new JLabel("The last received file is: ");
        jlSentText.setFont(new Font("Arial", Font.BOLD, 20));

        jlSentFile = new JLabel("");
        jlSentFile.setFont(new Font("Arial", Font.BOLD, 20));

        jpSent.add(jlSentText);
        jpSent.add(jlSentFile);

        JPanel jpDel = new JPanel();
        jpDel.setLayout(new FlowLayout(FlowLayout.LEFT,40,0));
        jpDel.setBorder(new EmptyBorder(50, 0, 0,0));

        JLabel jlDelText = new JLabel("The last deleted file is: ");
        jlDelText.setFont(new Font("Arial", Font.BOLD, 20));

        jlDelFile = new JLabel("");
        jlDelFile.setFont(new Font("Arial", Font.BOLD, 20));

        jpDel.add(jlDelText);
        jpDel.add(jlDelFile);

        // Adding everything to the main GUI.
        jFrame.add(jlTitle);
        jFrame.add(jpStatus);
        jFrame.add(jpNumOfClients);
        jFrame.add(jpSent);
        jFrame.add(jpDel);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
         try {
             new ServerMain();
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    public JLabel getJlNumber() {
        return jlNumber;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public JLabel getJlSentFile() {
        return jlSentFile;
    }

    public JLabel getJlDelFile() {
        return jlDelFile;
    }
}

