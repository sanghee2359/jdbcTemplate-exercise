package dao;

import domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;

public class UserDao { // 생성자를 입력안했네요. user을 초기화하는 부분
    private ConnectionImple connectionImple; // 인터페이스

    public UserDao(ConnectionImple connectionImple){
        this.connectionImple = connectionImple;
    }
    // getcount , deleteall 메소드 작성해볼게요.
    public void deleteAll() throws SQLException, ClassNotFoundException {    // delete는 add와 로직이 비슷합니다
        Connection c = null;
        PreparedStatement ps = null; //users 테이블 안의 모든 값 삭제
        try {
            c = connectionImple.makeConnection();
            ps = c.prepareStatement("DELETE FROM users;");
            ps.executeUpdate(); // 묶고 Ctrl+Alt+T 누르면 자동완성되여
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        }
        ps.close();
        c.close();
    }
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
        }
        rs.close();
        ps.close();
        c.close();
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
        }
        // user 테이블에 id, name, password 넣고 ?,?,?는 해당 값을 넣는다는 의미입니다
        ps.close();
        c.close();
    }
    // 여기도 id를 입력받아야해요
    public User get(String id) throws ClassNotFoundException, SQLException { // id로 user을 찾는 메소드
        Connection c = connectionImple.makeConnection();
        PreparedStatement ps = c.prepareStatement("SELECT id, name, password FROM users WHERE id=?");
        ps.setString(1, id); // set을 해야하는 부분인데 get을하고있었네요
        // id로 user 정보를 찾는다는 의미입니다
        // 찾은 user 정보를 담기 위해 선언합니다. select를 할때는 resultset을 해주어야 해요
        ResultSet rs = ps.executeQuery();
        // user가 null일 경우 exception 예외 처리를 해줍니다
        User user = null;
        if(rs.next()){  // 다음 User 정보를 가져오면서 만약
            user = new User(rs.getString("id"),rs.getString("name"),rs.getString("password"));
        }
        // user을 초기화 해주고 리턴해줍니다.
        // 선언한 순서대로 다시 종료
        rs.close();
        ps.close();
        c.close();
        // 테스트 코드 만들어볼게요
        if(user == null) throw new EmptyResultDataAccessException(1); // 다음 정보가 없으면 Exception 처리
        return user;
    }
    //다음은 try catch finally 입니다 어떠한 에러가 발생해도 리소스(자원) - rs, ps, c 등을 반환하도록 작업해주는 것입니다.
}
