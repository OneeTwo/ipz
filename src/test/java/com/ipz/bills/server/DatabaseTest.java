package com.ipz.bills.server;

import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        try (Connection connection = Database.getConnection()) {

            System.out.println("Database connection successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}