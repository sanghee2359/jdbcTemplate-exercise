package dao.Strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStrategy implements StatementStrategy { // alt + enter
    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        return c.prepareStatement("Delete From users");
    }
    // 잘 보시면 이렇게 sql문을 입력하는 PreparedStatment를 반환하죠
    // 인터페이스에서 이러한 뼈대를 만들어주는데,
    // 무엇이 반환되어야 하는지, 무엇을 입력받아야하는지 기억하고 있으면 헷갈리지 않아요
}
