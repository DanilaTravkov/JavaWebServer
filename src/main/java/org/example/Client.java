package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static final int PORT = 3000;
    public static final String HOST = "127.0.0.1";


    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName(HOST);

            Socket socket = new Socket(addr, PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println("Client says hello");
            String response = in.readLine();
            System.out.println("Server responded with: " + response);

            in.close();
            out.close();
            socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
