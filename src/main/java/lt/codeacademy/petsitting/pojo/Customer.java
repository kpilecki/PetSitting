package lt.codeacademy.petsitting.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class Customer extends User{

    private String firstName;
    private String lastName;

}
