package com.mycompany.esms.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    // Shared connection
    public static Connection con;

    // All initialization logic is kept compact inside this single constructor
    public DAO() {
        if (con == null) {
            if (con == null) {
                // Các biến cấu hình kết nối được khai báo cục bộ trong phương thức này
                String jdbcUrl = "jdbc:mysql://localhost:3306/esms?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
                String jdbcUsername = "root";
                String jdbcPassword = "anacondaxs5";
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException("Failed to initialize DB connection", e);
                }
            }
        }
    }
} 
