package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        try {
            final Socket[] socket = new Socket[1];
            final BufferedReader[] input = new BufferedReader[1];
            final PrintWriter[] output = new PrintWriter[1];
            final File[] fileToSend = new File[1];

            final Socket[] dataSocket = new Socket[1];
            final BufferedReader[] inputData = new BufferedReader[1];
            final PrintWriter[] outputData = new PrintWriter[1];

            JFrame jFrame = new JFrame("FTP Client");
            jFrame.setSize(700, 700);
            jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int x = dim.width / 2 - jFrame.getSize().width / 2;
            int y = dim.height / 2 - jFrame.getSize().height / 2;
            jFrame.setLocation(x + 500, y);
            jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            JLabel jlTitle = new JLabel("File Sender");
            jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
            // Add a border around the label for spacing.
            jlTitle.setBorder(new EmptyBorder(20,0,10,0));
            jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Label on how to use the program
            JLabel jlMessage = new JLabel("To use the commands you first need to connect to the server: ");
            jlMessage.setFont(new Font("Arial", Font.BOLD, 20));
            // Add a border around the label for spacing.
            jlMessage.setBorder(new EmptyBorder(30,0,10,0));
            jlMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

            // panel for username
            JPanel jpUser = new JPanel();
            jpUser.setBorder(new EmptyBorder(50, 0, 25, 0));
            jpUser.setLayout(new FlowLayout(FlowLayout.LEFT,40,0));

            // label for username
            JLabel jlUserName = new JLabel("Username");
            jlUserName.setFont(new Font("Arial", Font.BOLD, 20));

            // text area for username
            JTextArea userText = new JTextArea();
            userText.setColumns(10);

            // panel for password
            JPanel jpPass = new JPanel();
            jpPass.setBorder(new EmptyBorder(25, 0, 50, 0));
            jpPass.setLayout(new FlowLayout(FlowLayout.LEFT,40,0));

            // label for password
            JLabel jlPass = new JLabel("Password");
            jlPass.setFont(new Font("Arial", Font.BOLD, 20));

            // text are for password
            JTextArea passText = new JTextArea();
            passText.setColumns(10);

            JLabel jlFileName = new JLabel("The file you want to send is: ");
            jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
            jlFileName.setBorder(new EmptyBorder(20,0,20,0));
            jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel jlFileNameDel = new JLabel("The file you want to delete is: ");
            jlFileNameDel.setFont(new Font("Arial", Font.BOLD, 20));
            jlFileNameDel.setBorder(new EmptyBorder(20,0,20,0));
            jlFileNameDel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // connect button
            JButton jbConnect = new JButton("Connect");
            jbConnect.setPreferredSize(new Dimension(125, 50));
            jbConnect.setBorder(new EmptyBorder(0, 0, 0, 0));
            jbConnect.setFont(new Font("Arial", Font.BOLD, 20));

            jbConnect.addActionListener(e -> {
                String username = userText.getText();
                String password = passText.getText();
                if (username.equals("Aleksa") && password.equals("Networking1")) {
                    try {
                        if (socket[0] != null && socket[0].isConnected()) {
                            JOptionPane.showMessageDialog(null, "You are already connected");
                            return;
                        }
                        socket[0] = new Socket("localhost", 2021);
                        input[0] = new BufferedReader(new InputStreamReader(socket[0].getInputStream()));
                        output[0] = new PrintWriter(socket[0].getOutputStream(), true);

                        dataSocket[0] = new Socket("localhost", 2020);
                        inputData[0] = new BufferedReader(new InputStreamReader(dataSocket[0].getInputStream()));
                        outputData[0] = new PrintWriter(dataSocket[0].getOutputStream(), true);

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "331 Username OK " + "\n" +
                            "231 User Logged In" + "\n"+ "Your are now connected to the server");
                }
                else JOptionPane.showMessageDialog(null, "530 Invalid username or password");
            });

            jpUser.add(jlUserName);
            jpUser.add(userText);
            jpPass.add(jlPass);
            jpPass.add(passText);

            JLabel jlCommands = new JLabel("Commands");
            jlCommands.setFont(new Font("Arial", Font.BOLD, 30));
            jlCommands.setBorder(new EmptyBorder(0,0,30,0));
            jlCommands.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Panel that contains the buttons.
            JPanel jpButton = new JPanel();
            GridLayout grid = new GridLayout(3,3);
            grid.setHgap(20);
            grid.setVgap(10);
            jpButton.setLayout(grid);
            jpButton.setBorder(new EmptyBorder(0,20,20,20));

            // SYST COMMAND
            JButton jbSyst = new JButton("SYST");
            jbSyst.setPreferredSize(new Dimension(100, 50));
            jbSyst.setFont(new Font("Arial", Font.BOLD, 20));

            jbSyst.addActionListener(e -> {
                try {
                    output[0].println("SYST");
                    String msg = input[0].readLine();
                    JOptionPane.showMessageDialog(null, msg);
                } catch (IOException | NullPointerException exception ) {
                    JOptionPane.showMessageDialog(null, "You must be connected to the server");
                }
            });

            // LIST COMMAND
            JButton jbList = new JButton("LIST");
            jbList.setPreferredSize(new Dimension(100, 50));
            jbList.setFont(new Font("Arial", Font.BOLD, 20));

            jbList.addActionListener(e -> {
                try {
                    output[0].println("LIST");
                    String msg = input[0].readLine();
                    String list = inputData[0].readLine();
                    JOptionPane.showMessageDialog(null, msg + "\n" + list) ;
                } catch (IOException | NullPointerException exception ) {
                    JOptionPane.showMessageDialog(null, "You must be connected to the server");
                }

            });

            // STOR COMMAND
            JButton jbSendFile = new JButton("Send File");
            jbSendFile.setPreferredSize(new Dimension(100, 50));
            jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));
            JButton jbChooseFile = new JButton("Choose File");
            jbChooseFile.setPreferredSize(new Dimension(100, 50));
            jbChooseFile.setFont(new Font("Arial", Font.BOLD, 20));

            jbChooseFile.addActionListener(e -> {

                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file to send.");
                if (jFileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    jlFileName.setText("The file you want to send is: " + fileToSend[0].getName());
                }
            });

            jbSendFile.addActionListener(e -> {
                if (fileToSend[0] == null) jlFileName.setText("You must choose a file first");
                else {
                    FileReader fileReader;
                    try {
                        fileReader = new FileReader(fileToSend[0].getAbsolutePath());
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        int numberOfLines = 0;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line + "\n");
                            numberOfLines++;
                        }
                        output[0].println("STOR " + fileToSend[0].getName());
                        output[0].println(numberOfLines);
                        outputData[0].println(stringBuilder);
                        bufferedReader.close();
                        String conCode = input[0].readLine();
                        JOptionPane.showMessageDialog(null, conCode);

                    } catch (IOException | NullPointerException exception ) {
                        JOptionPane.showMessageDialog(null, "You must be connected to the server");
                    }
                }
            });

            // DELE COMMAND
            JButton jbDele = new JButton("DELE");
            jbDele.setPreferredSize(new Dimension(100, 50));
            jbDele.setFont(new Font("Arial", Font.BOLD, 20));

            jbDele.addActionListener(e -> {
                String fileName = JOptionPane.showInputDialog(
                        "Enter the name of the file you want to delete:\n",
                        "");
                try {
                    output[0].println("DELE " + fileName);
                    String msg = input[0].readLine();
                    JOptionPane.showMessageDialog(null, msg);
                } catch (IOException | NullPointerException exception) {
                    JOptionPane.showMessageDialog(null, "You must be connected to the server first");
                }

            });

            // QUIT COMMAND
            JButton jbQuit = new JButton("QUIT");
            jbQuit.setPreferredSize(new Dimension(100, 50));
            jbQuit.setFont(new Font("Arial", Font.BOLD, 20));

            jbQuit.addActionListener(e -> {
                try {
                    output[0].println("QUIT");
                    String msg = input[0].readLine();
                    JOptionPane.showMessageDialog(null, msg);
                    socket[0].close();
                    System.exit(0);
                } catch (IOException | NullPointerException exception) {
                    JOptionPane.showMessageDialog(null, "You must be connected to the server first");
                }
            });

            // Add the buttons to the panel
            jpButton.add(jbSyst);
            jpButton.add(jbList);
            jpButton.add(jbChooseFile);
            jpButton.add(jbSendFile);
            jpButton.add(jbDele);
            jpButton.add(jbQuit);

            // Add everything to the frame and make it visible.
            jFrame.add(jlTitle);
            jFrame.add(jlMessage);
            jFrame.add(jpUser);
            jFrame.add(jbConnect);
            jFrame.add(jpPass);
            jFrame.add(jlCommands);
            jFrame.add(jpButton);
            jFrame.add(jlFileName);
            jFrame.setVisible(true);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
