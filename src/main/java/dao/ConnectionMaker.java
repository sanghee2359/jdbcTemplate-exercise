package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class ConnectionMaker {
    public Connection openConnection() throws ClassNotFoundException, SQLException {
        Map<String, String> env = System.getenv();
        String dbHost = env.get("DB_HOST");
        String dbUser = env.get("DB_HOST");
        String dbPassword = env.get("DB_HOST");

        Class.forName("com.mysql.cj.jdbc.Driver");  // Alt+ enter

        Connection c = DriverManager.getConnection(dbHost, dbUser, dbPassword);
        return c;
    }
} // 여기까지 오셨나요? 다음으로 넘어갈게요
