package dao.Strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException; // 바뀌는 sql문을 담을 메소드
}
