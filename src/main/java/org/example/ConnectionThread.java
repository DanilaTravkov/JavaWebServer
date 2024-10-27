package org.example;

import java.io.*;
import java.net.Socket;

public class ConnectionThread extends Thread {
    private final Socket socket;

    public ConnectionThread(Socket _socket) {
        this.socket = _socket;
    }

    @Override
    public void run() {
        handleHttpRequest(socket);
    }

    public static void handleHttpRequest(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            if (requestLine != null && !requestLine.isEmpty()) {
                String[] requestParts = requestLine.split(" ");
                String path = requestParts[1];

                System.out.println("Path: " + path);

                if (path.equals("/")) {
                    sendHtmlResponse(out, "/templates/home.html");
                } else if (path.equals("/json")) {
                    sendJsonResponse(out);
                } else {
                    sendHtmlResponse(out, "/templates/404.html", 404);
                }
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendJsonResponse(OutputStream out) throws IOException {
        String jsonResponse = "{\"message\": \"Hello from JSON!\"}";
        byte[] responseBytes = jsonResponse.getBytes();

        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write("Content-Type: application/json\r\n".getBytes());
        out.write(("Content-Length: " + responseBytes.length + "\r\n").getBytes());
        out.write("\r\n".getBytes());

        out.write(responseBytes);
        out.flush();
    }

    private static void sendHtmlResponse(OutputStream out, String resourcePath) throws IOException {
        sendHtmlResponse(out, resourcePath, 200);
    }

    private static void sendHtmlResponse(OutputStream out, String resourcePath, int statusCode) throws IOException {
        InputStream htmlResponse = ConnectionThread.class.getResourceAsStream(resourcePath);
        if (htmlResponse != null) {
            byte[] contentBytes = htmlResponse.readAllBytes();
            int contentLength = contentBytes.length;

            out.write(("HTTP/1.1 " + statusCode + (statusCode == 200 ? " OK" : " Not Found") + "\r\n").getBytes());
            out.write("Content-Type: text/html\r\n".getBytes());
            out.write(("Content-Length: " + contentLength + "\r\n").getBytes());
            out.write("\r\n".getBytes());

            out.write(contentBytes);
            out.flush();
        } else {
            InputStream htmlResponse404 = ConnectionThread.class.getResourceAsStream("/templates/404.html");
            if (htmlResponse404 != null) {
                byte[] contentBytes = htmlResponse404.readAllBytes();
                int contentLength = contentBytes.length;

                out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                out.write("Content-Type: text/html\r\n".getBytes());
                out.write(("Content-Length: " + contentLength + "\r\n").getBytes());
                out.write("\r\n".getBytes());

                out.write(contentBytes);
                out.flush();
            }
        }
    }

}
