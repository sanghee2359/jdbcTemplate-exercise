package dao;

import dao.DBInterface.ConnectionImple;
import dao.Strategy.DeleteAllStrategy;
import dao.Strategy.StatementStrategy;
import domain.User;

import java.sql.*;
//try-catch-finally 까지 진행하고 `바뀌는 부분`만 Strategy에 정리했습니다.

// try catch finally가 너무 계속 반복되면서 저희처럼 복붙하다보면 실수가 일어날 수 있어요.
// 이걸 휴먼에러라고 하는데, 이걸 막기위해 메소드로 정리했어요.
// 이렇듯 바뀌지 않고 계속해서 반복되는 작업을 컨텍스트 context라고 한답니다.
// 바뀌않는 컨텍스트안에서 계속해서 바뀌는 흐름이 있었어요. add() get() 에서 sql문이 서로 다르죠.
// 이렇듯 바뀌는 부분을 전략이라고 하는데, 외부 인터페이스를 만들어서 작업을 위임하는 방식이에요

public class UserDao {
    private ConnectionImple connectionImple; // 인터페이스
    // 생성자를 입력안했네요. DB커넥터를 초기화하는 부분
    public UserDao(ConnectionImple connectionImple){
        this.connectionImple = connectionImple;
    }

    // 바뀌지 않는 부분
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            c = connectionImple.makeConnection();
            pstmt = stmt.makePreparedStatement(c);
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally { // error가 나도 실행되는 블록
            if(pstmt != null) { try { pstmt.close();} catch (SQLException e) {}}
            if(c != null){ try {c.close();} catch (SQLException e) {}}
        }
    }
    // getcount , deleteall 메소드 작성해볼게요.
    public void deleteAll() throws SQLException, ClassNotFoundException {    // delete는 add와 로직이 비슷합니다
        jdbcContextWithStatementStrategy(new DeleteAllStrategy());
    }
    // Add역시 jdbcContextWithStatementStrategy 메소드로 바꿀 수 있는데, 한번 시도해보시길 추런드립니다.
    // 힌트는 jdbcConext 이 메소드에 무엇을 파라매터로 받고, 무엇을 리턴하는지 살펴보시거나
    // 저희 회고에 정리되어 있으니 읽어보시는 것도 추천드려요
    public void add(User user) throws ClassNotFoundException, SQLException
    {
        // db연결 겹치는 부분 -> interface로 수정
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = connectionImple.makeConnection();
            ps = c.prepareStatement("INSERT INTO users(id, name, password) VALUES (?,?,?);");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }
    // 모든 메소드에 trycatchfinally함니다
    public int getCount() throws SQLException, ClassNotFoundException { // count는 SELECT sql문을 사용하기 때문에 get과 로직이 비슷해요
        Connection c = null;
        PreparedStatement ps = null; // count한 값을 출력하니 int로 반환해야겠네요
        ResultSet rs = null;
        int count = 0;
        try {
            c = connectionImple.makeConnection();
            ps = c.prepareStatement("SELECT count(*) FROM users;");
            rs = ps.executeQuery();
            rs.next(); // int형반환..
            count = rs.getInt(1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
        return count;
    }
    // 여기도 id를 입력받아야해요
    // try-catch-finally 중 finally는 예외가 발생하더라도 무조건 실행하는 부분이라 리소스를 반환하는 로직을 담아요
    public User get(String id) throws ClassNotFoundException, SQLException { // id로 user을 찾는 메소드
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user;
        try {
            c = connectionImple.makeConnection();
            ps = c.prepareStatement("SELECT id, name, password FROM users WHERE id=?");
            ps.setString(1, id); // set을 해야하는 부분인데 get을하고있었네요
            // id로 user 정보를 찾는다는 의미입니다
            // 찾은 user 정보를 담기 위해 선언합니다. select를 할때는 resultset을 해주어야 해요
            rs = ps.executeQuery();
            // user가 null일 경우 exception 예외 처리를 해줍니다
            user = null;
            if(rs.next()){  // 다음 User 정보를 가져오면서 만약
                user = new User(rs.getString("id"),rs.getString("name"),rs.getString("password"));
            }
            return user;
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
        // finally에 들어간 부분이니 삭제
    }
    //다음은 try catch finally 입니다 어떠한 에러가 발생해도 리소스(자원) - rs, ps, c 등을 반환하도록 작업해주는 것입니다.
}
