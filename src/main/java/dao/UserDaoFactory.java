package dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class UserDaoFactory { // 팩토리에서 spring을 도입할게에요
    /*@Bean
    public UserDao awsUserDao(){
        AWSConnectionImple awsConnectionImple = new AWSConnectionImple();
        UserDao userDao = new UserDao(awsConnectionImple);
        return userDao;
    }*/
    // dataSource를 팩토리에서 정의하는 것 같아보입니다.
    @Bean
    UserDao awsUserDao(){
        return new UserDao(awsDataSource());
    }

    @Bean // wow
    DataSource awsDataSource() {
        Map<String, String> env = System.getenv(); // 이 구조 처음 DB연결할 때 보신 적 있죠?? 그부분 이랑 동일해요
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(); // 이거 다르네요
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl(env.get("DB_HOST")); // env.get으로 값을 넣어줘야하나 봅니다
        dataSource.setUsername(env.get("DB_USER"));
        dataSource.setPassword(env.get("DB_PASSWORD"));
        return dataSource;
    }
    // 이제 factory에서 정의가 끝났으니
    // userDao에서 사용하기 위해 변경되는 사항을 보여드리게요
}
