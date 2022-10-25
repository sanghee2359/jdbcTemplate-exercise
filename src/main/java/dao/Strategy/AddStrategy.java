package dao.Strategy;

import domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStrategy implements StatementStrategy {
    // add(User user)메소드는 User정보를 파라매터로 받기 때문에
    // 생성자로 User 객체를 받아야 user정보에 접근하고 값 넣기가 가능해요
    private User user;
    public AddStrategy(User user){
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("INSERT INTO users(id, name, password) VALUES (?,?,?);");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        return ps;
    }
}
