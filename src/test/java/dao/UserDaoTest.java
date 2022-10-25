package dao;

import domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {
    // 잠시만요 테스트 전에 Connection 분리부터 할게요
    // 1. class 분리
    // 2. interface 분리
    // 3. Spring 적용 + ApplicationCoontext적용
    @Autowired
    ApplicationContext context;
    UserDao userDao;
    User user1;
    User user2;
    User user3;
    @BeforeEach //이부분은 설정해주는 부분입니다. 메소드마다 반복되는 부분을 추출한거에요
    void setup() {
        userDao = context.getBean("awsUserDao", UserDao.class);
        user1 = new User("1", "정상희", "1106");
        user2 = new User("2", "신지원", "0129");
        user3 = new User("3", "박정현", "0308");
    }
    @Test
    @DisplayName("Add와 Get 테스트")
    void AddAndGet() throws SQLException, ClassNotFoundException {
        // 이부분을 바꿔줬던거 같아요
        userDao.add(user2);
        userDao.get("2");
        assertEquals("신지원",user2.getName()); // 이거 add해서 남아있는 겁니당

    }
    @Test
    @DisplayName("Delete와 Count 테스트")
    void DeleteAndCount() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());
    }

}