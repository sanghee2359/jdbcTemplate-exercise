package domain;

public class User { // user 도메인 클래스 완성
    private String id; // ctrl + d -> 복사입니다
    private String name;
    private String password;

    public User(String id, String name, String password) { // alt + insert
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
