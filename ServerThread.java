package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    private final Socket socket;
    private final ServerMain serverMain;
    private final ServerSocket serverDataSocket;
    private int num;
    BufferedReader input;
    PrintWriter output;
    Socket dataSocket;

    public ServerThread(Socket socket, main.ServerMain serverMain, ServerSocket serverDataSocket, int num) {
        this.socket = socket;
        this.serverMain = serverMain;
        this.serverDataSocket = serverDataSocket;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            dataSocket = serverDataSocket.accept();

            serverMain.getJlNumber().setText(Integer.toString(num));

            while (true) {
                String msg;
                msg = input.readLine();
                if (msg.startsWith("SYST")) {
                    String OS = System.getProperty("os.name");
                    output.println("215: " + OS + " System");
                }
                else if (msg.startsWith("STOR")) storeFile(msg);
                else if (msg.startsWith("LIST")) listFiles();
                else if (msg.startsWith("DELE")) deleteFile(msg);
                else if (msg.startsWith("QUIT")) {
                    try {
                        output.println("221 Closing Connection");
                        socket.close();
                        serverMain.getJlNumber().setText(Integer.toString(--num));
                    } catch (IOException e) {
                        System.out.println("Something went wrong, try agian");
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void storeFile(String msg) throws IOException {
        BufferedReader inputData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        int numberOfLines = Integer.parseInt(input.readLine());
        String fileName = msg.substring(5);

        for (File f : serverMain.getFiles()) {
            if (fileName.equals(f.getName())) {
                output.println("550: File already exist");
                return;
            }
        }

        File file = new File("./ServerFiles/" + fileName);
        PrintWriter writer = new PrintWriter(file);
        while (numberOfLines > 0) {
            writer.println(inputData.readLine());
            numberOfLines--;
        }
        writer.close();
        serverMain.getFiles().add(file);
        serverMain.getJlSentFile().setText(fileName);
        output.println("250 Completed: File successfully sent");
    }

    private void deleteFile(String msg) throws IOException {
        String fileName = msg.substring(5);
        for (File f : serverMain.getFiles()) {
            if (f.getName().equals(fileName)) {
                if (!f.delete()) {
                    output.println("451 LocalError: Try again");
                    break;
                }
                serverMain.getFiles().remove(f);
                serverMain.getJlDelFile().setText(fileName);
                output.println("250 Completed: File successfully deleted");
                break;
            }
        }
        output.println("550: File does not exist");
    }

    private void listFiles() throws IOException {
        PrintWriter outputData = new PrintWriter(new PrintWriter(dataSocket.getOutputStream()),true);
        StringBuilder stringBuilder = new StringBuilder();
        int numOfFiles = serverMain.getFiles().size();
        int counter = 0;
        for (File f : serverMain.getFiles()) {
            counter++;
            String sign = (counter < numOfFiles) ? ", " : ".";
            stringBuilder.append(f.getName() + sign);
        }
        output.println("200 Completed:");
        outputData.println("The files in the directory are: " + stringBuilder);
    }
}
