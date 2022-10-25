package dao.DBInterface;

// DB 커넥터 2. 인터페이스의 구현체

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class AWSConnectionImple implements ConnectionImple {
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Map<String, String> env = System.getenv();
        String dbHost = env.get("DB_HOST");
        String dbUser = env.get("DB_USER");
        String dbPassword = env.get("DB_PASSWORD");

        Class.forName("com.mysql.cj.jdbc.Driver");  // Alt+ enter

        Connection c = DriverManager.getConnection(dbHost, dbUser, dbPassword);
        return c;
    }
}
