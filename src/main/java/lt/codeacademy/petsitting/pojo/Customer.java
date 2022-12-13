package lt.codeacademy.petsitting.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends User{

    private String firstName;
    private String lastName;

    @Builder

    public Customer(String username, String password, String firstName, String lastName) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
