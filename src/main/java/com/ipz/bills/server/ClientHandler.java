package com.ipz.bills.server;

import java.io.*;
import java.net.Socket;
import java.sql.*;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(),
                        true
                )
        ) {

            String request;

            while ((request = reader.readLine()) != null) {

                System.out.println("Request: " + request);

                String response = processRequest(request);

                writer.println(response);

                System.out.println("Response: " + response);
            }

        } catch (IOException e) {
            System.out.println(
                    "Client disconnected: " + e.getMessage()
            );
        }
    }

    private String processRequest(String request) {

        try {

            String[] parts = request.split("\\|");

            String command = parts[0];

            return switch (command) {

                case "REGISTER" -> register(parts);

                case "LOGIN" -> login(parts);

                case "PAY" -> pay(parts);

                case "HISTORY" -> history(parts);

                default -> "ERROR|Unknown command";
            };

        } catch (Exception e) {

            return "ERROR|" + e.getMessage();
        }
    }

    private String register(String[] parts) throws SQLException {

        if (parts.length < 3) {
            return "ERROR|Invalid REGISTER request";
        }

        String username = parts[1];
        String password = parts[2];

        String sql =
                "INSERT INTO users(username, password) VALUES (?, ?)";

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();

            return "REGISTER_SUCCESS";

        } catch (SQLIntegrityConstraintViolationException e) {

            return "ERROR|User already exists";
        }
    }

    private String login(String[] parts) throws SQLException {

        if (parts.length < 3) {
            return "ERROR|Invalid LOGIN request";
        }

        String username = parts[1];
        String password = parts[2];

        String sql =
                "SELECT id FROM users " +
                        "WHERE username = ? AND password = ?";

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return "LOGIN_SUCCESS";
            }

            return "ERROR|Incorrect username or password";
        }
    }

    private String pay(String[] parts) throws SQLException {

        if (parts.length < 5) {
            return "ERROR|Invalid PAY request";
        }

        String username = parts[1];
        String type = parts[2];
        String details = parts[3];

        double amount = Double.parseDouble(parts[4]);

        if (amount <= 0) {
            return "ERROR|Invalid amount";
        }

        String sql =
                "INSERT INTO payments" +
                        "(username, type, details, amount) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);
            statement.setString(2, type);
            statement.setString(3, details);
            statement.setDouble(4, amount);

            statement.executeUpdate();

            return "PAYMENT_SUCCESS";
        }
    }

    private String history(String[] parts) throws SQLException {

        if (parts.length < 2) {
            return "ERROR|Invalid HISTORY request";
        }

        String username = parts[1];

        String sql =
                "SELECT type, details, amount " +
                        "FROM payments " +
                        "WHERE username = ? " +
                        "ORDER BY created_at DESC";

        StringBuilder result = new StringBuilder();

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                if (!result.isEmpty()) {
                    result.append(";");
                }

                result
                        .append(resultSet.getString("type"))
                        .append(",")
                        .append(resultSet.getString("details"))
                        .append(",")
                        .append(resultSet.getDouble("amount"));
            }
        }

        return "HISTORY|" + result;
    }
}