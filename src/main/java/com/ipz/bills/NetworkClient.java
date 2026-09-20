package com.ipz.bills;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static String send(String request) throws Exception {

        try (
                Socket socket = new Socket(HOST, PORT);

                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(),
                        true
                );

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                )
        ) {
            writer.println(request);
            return reader.readLine();
        }
    }
}