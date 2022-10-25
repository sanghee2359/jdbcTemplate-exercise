package dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext { // JdbcContext도 DB연결하기 위해 DataSource 정보를 방아야해요
    private DataSource dataSource;
    public JdbcContext(DataSource dataSource){
        this.dataSource = dataSource;
    }
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            c = dataSource.getConnection(); // datasource는 getConnection()이라는 메소드가
            pstmt = stmt.makePreparedStatement(c); //정의되어있습니다
            pstmt.executeUpdate();
        } catch (SQLException e) { // 여기도 SQlException만 잡는걸 알수있죠?
            throw new RuntimeException(e);
        } finally { // error가 나도 실행되는 블록
            if(pstmt != null) { try { pstmt.close();} catch (SQLException e) {}}
            if(c != null){ try {c.close();} catch (SQLException e) {}}
        }
    }
    public void executeSql(final String sql) throws SQLException {
        this.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement(sql);
            }
        });
    }
}
