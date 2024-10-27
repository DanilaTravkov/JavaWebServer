package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 3000;
    public static final String HOST = "127.0.0.1";

    public static void main(String[] args) {

        try(ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("Listening on port " + PORT);
            while (true) {
                Socket clientSocket = ss.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                Thread connectionThread = new ConnectionThread(clientSocket);
                connectionThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
