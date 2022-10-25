package dao;

import dao.DBInterface.ConnectionImple;
import domain.User;

import java.sql.*;
//try-catch-finally 부터 시작할게요
// try catch finally가 너무 계속 반복되면서 저희처럼 복붙하다보면 실수가 일어날 수 있어요
// 변하지 않고 계속 반복되는 걸 컨텍스트 context라고 한답니다
// 바뀌는 부분을 전략이라고 하는데, 외부 인터페이스를 만들어서 작업을 위임하는 방식이에요
// 메소드로 추출해볼게요
public class UserDao {
    private ConnectionImple connectionImple; // 인터페이스
    // 생성자를 입력안했네요. DB커넥터를 초기화하는 부분
    public UserDao(ConnectionImple connectionImple){
        this.connectionImple = connectionImple;
    }

    // getcount , deleteall 메소드 작성해볼게요.
    public void deleteAll() throws SQLException, ClassNotFoundException {    // delete는 add와 로직이 비슷합니다
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = connectionImple.makeConnection();
            ps = c.prepareStatement("DELETE FROM users");
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
    public void add(User user) throws ClassNotFoundException, SQLException { // user값을 넣는 메소드
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
