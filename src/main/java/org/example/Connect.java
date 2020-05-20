package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static Connection getJDBCConnection() throws SQLException {
        final String url="jdbc:mysql://localhost:3306/exam3";
        final String user="root";
        final String password="tranthivietnga";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    };
}
