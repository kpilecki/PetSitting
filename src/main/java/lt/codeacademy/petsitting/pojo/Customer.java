package lt.codeacademy.petsitting.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Customer extends User{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )

    private String firstName;
    private String lastName;

    @Builder

    public Customer(String username, String password, String firstName, String lastName) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
