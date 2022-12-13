package lt.codeacademy.petsitting.pojo;

import lombok.*;

@Getter
@Setter
public class User {

    private String username;
    private String password;

    @Builder

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
