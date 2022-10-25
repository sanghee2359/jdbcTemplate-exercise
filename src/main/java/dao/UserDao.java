package dao;

import domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
//try-catch-finally 까지 진행하고 `바뀌는 부분`만 Strategy에 정리했습니다.

// try catch finally가 너무 계속 반복되면서 저희처럼 복붙하다보면 실수가 일어날 수 있어요.
// 이걸 휴먼에러라고 하는데, 이걸 막기위해 메소드로 정리했어요.
// 이렇듯 바뀌지 않고 계속해서 반복되는 작업을 컨텍스트 context라고 한답니다.
// 바뀌않는 컨텍스트안에서 계속해서 바뀌는 흐름이 있었어요. add() get() 에서 sql문이 서로 다르죠.
// 이렇듯 바뀌는 부분을 전략이라고 하는데, 외부 인터페이스를 만들어서 작업을 위임하는 방식이에요

public class UserDao {
    private ConnectionImple connectionImple; // 인터페이스
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    // 1. 필드 선언
    // 2. 생성자에서 초기화하기
    // 3. 클래스.메소드() 형태로 변경해주기
    public UserDao(DataSource dataSource){

//        this.connectionImple = connectionImple; // 기존에 쓴 DB커넥터 인터페이스 삭제
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
        // 저희가 아까 datasorce로 변경하고나서 다른 메소드 안의 DB커넥터는 변경을 안해줬거든요
        // 그거 마저 변경하면 될거같아요

    public void deleteAll() {
        jdbcTemplate.update("Delete From users");
    }
    // 여기까지가 이 바로 옆의 의존관계의 모습이에요
    public void add(User user) {
        // db연결 겹치는 부분 -> interface로 수정
        jdbcTemplate.update(" INSERT INTO users(id, name, password) VALUES(?, ?, ?)",user.getId(),user.getName(),user.getPassword());
    }
    public int getCount() { // count는 SELECT sql문을 사용하기 때문에 get과 로직이 비슷해요
        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM users;", Integer.class);
    }
    // 여기도 id를 입력받아야해요
    // try-catch-finally 중 finally는 예외가 발생하더라도 무조건 실행하는 부분이라 리소스를 반환하는 로직을 담아요
    public User get(String id) { // id로 user을 찾는 메소드
        String sql = "SELECT id, name, password FROM users WHERE id=?";
        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
                return user;
            }
        };
        return this.jdbcTemplate.queryForObject(sql, rowMapper, id);
        // finally에 들어간 부분이니 삭제
    }
    //다음은 try catch finally 입니다 어떠한 에러가 발생해도 리소스(자원) - rs, ps, c 등을 반환하도록 작업해주는 것입니다.
}
