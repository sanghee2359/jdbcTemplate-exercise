package dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDaoFactory { // 팩토리에서 spring을 도입할게에요
    @Bean
    public UserDao awsUserDao(){
        AWSConnectionImple awsConnectionImple = new AWSConnectionImple();
        UserDao userDao = new UserDao(awsConnectionImple);
        return userDao;
    }
}
